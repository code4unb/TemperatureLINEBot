package com.code4unb.TemperatureLINEBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@LineMessageHandler
public class MessageController {
    MessageAction waitingAction;
    @EventMapping
    public Message handleFollowEvent(FollowEvent event) {
        return showRegister();
    }
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        if(waitingAction!=null) {
            Message message = waitingAction.Action(event);
            if (waitingAction.Succeed) waitingAction = null;
            return message;
        }

        return showRegister();
    }
    private FlexContainer loadFlexContainer(String name){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(loadMessageJson(name), FlexContainer.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Message showRegister(){
        waitingAction = new MessageAction() {
            @Override
            public Message Action(MessageEvent<TextMessageContent> event) {
                String message = event.getMessage().getText();
                UserData user;
                if((user = tryParse(message))!=null){
                    Succeed = true;
                    return TextMessage.builder().text(user.toString()+" 登録しました。 間違いがある場合は '再登録' と送信してください。").build();
                }else{
                    return TextMessage.builder().text("入力に不備があります。再度入力してください。").build();
                }
            }
            private UserData tryParse(String text){
                String[] args = text.split(" ");
                if(args.length <5) return null;
                try{
                    UserData.Grades grade = UserData.Grades.values()[ Integer.parseInt(args[0])-1];
                    int class_ = Integer.parseInt(args[1]);
                    int number = Integer.parseInt(args[2]);
                    String firstName = args[4];
                    String lastName = args[3];
                    return new UserData(grade,class_,number,firstName,lastName);
                }catch(Exception e){
                    return null;
                }
            }
        };
        return new FlexMessage("register",loadFlexContainer("register"));
    }

    private String loadMessageJson(String name){
        String path = "/messages/"+name+".json";
        try (InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"))) {
            StringBuilder result = new StringBuilder() ;
            String str = br.readLine();
            while(str != null){
                result.append(str);
                if((str = br.readLine()) != null){
                };
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private abstract class MessageAction{
        public boolean Succeed = false;
        public abstract Message Action(MessageEvent<TextMessageContent> event);
    }
}
