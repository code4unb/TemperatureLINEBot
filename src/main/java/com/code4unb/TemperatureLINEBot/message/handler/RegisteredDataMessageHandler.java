package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.db.UserDataRepository;
import com.code4unb.TemperatureLINEBot.db.entity.UserData;
import com.code4unb.TemperatureLINEBot.message.SingleMessageHandler;
import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.util.FlexMessages;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RegisteredDataMessageHandler extends SingleMessageHandler {
    @Autowired
    UserDataRepository userDataRepository;

    public RegisteredDataMessageHandler(){
        super("登録情報");
    }
    @Override
    public List<Message> handleMessage(MessageReply message) {
        Optional<UserData> entity = userDataRepository.findByLineID(message.getSource().getUserId());
        if(entity.isPresent()){
            UserData data = entity.get();
            String json = FlexMessages.LoadJsonFromJsonFile("registered-data");
            json = String.format(json,data.getLastName(),data.getFirstName(),data.getGrade().ordinal()+1,data.getClass_(),data.getNumber());
            return Collections.singletonList(new FlexMessage("registered-data", FlexMessages.LoadContainerFromJson(json)));
        }else{
            return Collections.singletonList(TextMessage.builder().text("登録情報がありません。先に '登録' と入力してユーザー情報を登録してください。").build());
        }
    }
}
