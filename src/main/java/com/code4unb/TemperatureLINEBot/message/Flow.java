package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import lombok.Getter;

import java.util.List;

public abstract class Flow {
    public boolean shouldHandle(String phrase){
        return true;
    }

    public abstract List<Message> postHandle();

    public abstract FlowResult handle(ReceivedMessage message);
}
