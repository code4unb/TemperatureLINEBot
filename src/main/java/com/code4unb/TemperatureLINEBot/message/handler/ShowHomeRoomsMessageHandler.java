package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.SingleMessageHandler;
import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.model.UserData;
import com.code4unb.TemperatureLINEBot.util.InputMapping;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowHomeRoomsMessageHandler extends SingleMessageHandler {
    public ShowHomeRoomsMessageHandler(){
        super("対応ホームルーム一覧","クラス","対応","対応クラス","ホームルーム");
    }

    @Override
    public List<Message> handleMessage(MessageReply message) {
        return Collections.singletonList(TextMessage.builder()
                .text(InputMapping.getAllClassrooms().stream()
                        .sorted(Comparator.comparing(UserData.ClassRoom::getGrade).thenComparing(UserData.ClassRoom::getClass_))
                        .map(x->x.toString()).collect(Collectors.joining(",")))
                .build());
    }
}
