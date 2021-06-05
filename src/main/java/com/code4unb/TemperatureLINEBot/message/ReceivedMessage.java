package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@Value
public class ReceivedMessage {
    Instant TimeStamp;

    Source Source;

    String KeyPhrase;

    String[] Args;

    public static ReceivedMessage Build(MessageEvent<TextMessageContent> event){
        String[] messages = event.getMessage().getText().split(" ");
        String[] Args = messages.length>1 ? (String[])(Arrays.stream(messages).skip(1).toArray()) : null ;
        return new ReceivedMessage(event.getTimestamp(),event.getSource(),messages[0],Args);
    }
}
