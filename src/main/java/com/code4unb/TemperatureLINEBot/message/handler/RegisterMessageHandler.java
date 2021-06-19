package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.UserData;
import com.code4unb.TemperatureLINEBot.db.UserDataEntity;
import com.code4unb.TemperatureLINEBot.db.UserDataRepository;
import com.code4unb.TemperatureLINEBot.message.Flow;
import com.code4unb.TemperatureLINEBot.message.FlowMessageHandler;
import com.code4unb.TemperatureLINEBot.message.FlowResult;
import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.code4unb.TemperatureLINEBot.util.FlexJson;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RegisterMessageHandler extends FlowMessageHandler {
    @Autowired
    private UserDataRepository userDataRepository;

    public RegisterMessageHandler() {
        super("登録","register");
    }

    @Override
    protected List<Flow> initFlows() {
        return Collections.singletonList(
                new Flow(){

                    @Override
                    public Optional<List<Message>> postHandle() {
                        return Optional.empty();
                    }

                    @Override
                    public FlowResult handle(ReceivedMessage message) {
                        UserData userData;
                        if((userData =  tryParse(message.getSource().getUserId(),message.getPhrases())) !=null){
                            Optional<UserDataEntity> found = userDataRepository.findByLineID(userData.getLineID());
                            if(found.isPresent()){
                                userDataRepository.save(new UserDataEntity(found.get().getId(),userData));
                            }else{
                                userDataRepository.save(new UserDataEntity(userData));
                            }
                            return FlowResult.builder()
                                    .result(Optional.of(Collections.singletonList(TextMessage.builder().text(userData +" 登録しました。 間違いがある場合は '再登録' と送信してください。").build())))
                                    .succeed(true)
                                    .build();
                        }else{
                            return FlowResult.builder()
                                    .result(Optional.of(Collections.singletonList(TextMessage.builder().text("入力に不備があります。再度入力してください。").build())))
                                    .succeed(false)
                                    .build();
                        }
                    }
                });
    }

    @Override
    protected List<Message> handleActivateMessage(ReceivedMessage message) {
        return Collections.singletonList(new FlexMessage("register",FlexJson.LoadFlexContainer("register")));
    }

    private UserData tryParse(String lineID,String[] args){
        if(args.length <5) return null;
        try{
            UserData.Grades grade = UserData.Grades.values()[ Integer.parseInt(args[0])-1];
            int class_ = Integer.parseInt(args[1]);
            int number = Integer.parseInt(args[2]);
            String firstName = args[4];
            String lastName = args[3];
            return new UserData(lineID,grade,class_,number,firstName,lastName);
        }catch(Exception e){
            return null;
        }
    }
}
