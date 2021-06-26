package com.code4unb.TemperatureLINEBot.model;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageReply extends Reply<MessageContent>{
    String [] phrases;

    String keyPhrase;

    @Nullable
    String[] args;

    public MessageReply(Instant timestamp, Source source, TextMessageContent message){
        super(timestamp,source,message);
        phrases = message.getText().split(" ");;
        keyPhrase = phrases[0];
        args = phrases.length>1 ? (Arrays.stream(phrases).skip(1).toArray(String[]::new)) : null ;;
    }

    public static MessageReply Build(MessageEvent<TextMessageContent> event){
        return new MessageReply(event.getTimestamp(),event.getSource(),event.getMessage());
    }
}
