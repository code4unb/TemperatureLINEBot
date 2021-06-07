package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.message.Message;
import lombok.Getter;

import java.util.Arrays;

public abstract class MessageHandlerBase{
    @Getter
    private final String KeyPhrase;

    @Getter
    private final String[] Aliases;

    public MessageHandlerBase(String keyPhrase,String... aliases){
        KeyPhrase = keyPhrase;
        Aliases = aliases;
    }

    public boolean shouldHandle(String phrase){
        if(KeyPhrase.equalsIgnoreCase(phrase))return true;
        return Arrays.stream(Aliases).anyMatch(x->x.equalsIgnoreCase(phrase));
    }

    public abstract Message HandleMessage(ReceivedMessage message);
}
