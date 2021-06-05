package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.UserData;
import com.code4unb.TemperatureLINEBot.message.Handler;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.code4unb.TemperatureLINEBot.message.SessionMessageHandler;
import com.code4unb.TemperatureLINEBot.util.FlexJson;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@Handler
public class RegisterMessageHandler extends SessionMessageHandler {
    public RegisterMessageHandler() {
        super("登録","register");
    }

    @Override
    protected Message HandleActivateMessage(ReceivedMessage message) {
        return new FlexMessage("register",FlexJson.LoadFlexContainer("register"));
    }

    @Override
    public Message HandleSessionMessage(ReceivedMessage message) {
        UserData userData;
        if((userData =  tryParse(message.getPhrases())) !=null){
            Close();
            return TextMessage.builder().text(userData.toString()+" 登録しました。 間違いがある場合は '再登録' と送信してください。").build();
        }else{
            return TextMessage.builder().text("入力に不備があります。再度入力してください。").build();
        }
    }
    private UserData tryParse(String[] args){
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
}
