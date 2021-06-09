package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.UserData;
import com.code4unb.TemperatureLINEBot.db.UserDataEntity;
import com.code4unb.TemperatureLINEBot.db.UserDataRepository;
import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.code4unb.TemperatureLINEBot.util.FlexJson;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RegisteredDataMessageHandler extends MessageHandler {
    @Autowired
    UserDataRepository userDataRepository;

    public RegisteredDataMessageHandler(){
        super("登録情報");
    }
    @Override
    public Message handleMessage(ReceivedMessage message) {
        Optional<UserDataEntity> entity = userDataRepository.findByLineID(message.getSource().getUserId());
        if(entity.isPresent()){
            UserData data = entity.get().toUserData();
            String json = FlexJson.LoadMessageJson("registered-data");
            json = String.format(json,data.getLastName(),data.getFirstName(),data.getGrade().ordinal()+1,data.getClass_(),data.getNumber());
            return new FlexMessage("registered-data",FlexJson.CreateFlexContainer(json));
        }else{
            return TextMessage.builder().text("登録情報がありません。先に '登録' と入力してユーザー情報を登録してください。").build();
        }
    }
}
