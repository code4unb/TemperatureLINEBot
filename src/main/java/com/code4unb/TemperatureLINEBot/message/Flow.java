package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;

import java.util.List;
import java.util.Optional;

public abstract class Flow {
    public boolean shouldHandle(String phrase){
        return true;
    }

    public abstract Optional<List<Message>> postHandle(Source source);

    public abstract FlowResult handle(MessageReply message);
}
