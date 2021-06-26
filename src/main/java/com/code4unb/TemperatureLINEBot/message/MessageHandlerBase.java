package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.linecorp.bot.model.message.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public abstract class MessageHandlerBase{
    @Getter
    private final String KeyPhrase;

    @Getter
    private final String[] Aliases;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private boolean hidden;

    public MessageHandlerBase(String keyPhrase,String... aliases){
        KeyPhrase = keyPhrase;
        Aliases = aliases;
    }

    public boolean shouldHandle(String phrase){
        if(KeyPhrase.equalsIgnoreCase(phrase))return true;
        return Arrays.stream(Aliases).anyMatch(x->x.equalsIgnoreCase(phrase));
    }

    public abstract List<Message> handleMessage(MessageReply message);
}
