package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.MessageHandlerBase;
import com.code4unb.TemperatureLINEBot.message.SingleMessageHandler;
import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class CommandMessageHandler extends SingleMessageHandler {
    @Autowired(required = false)
    Set<MessageHandlerBase> Handlers;

    public CommandMessageHandler() {
        super("Commands","コマンド");
    }

    @Override
    public List<Message> handleMessage(ReceivedMessage message) {
        String text = String.join(",",Handlers.stream().map(x->x.getKeyPhrase()).toArray(String[]::new));
        return Collections.singletonList(TextMessage.builder().text(text).build());
    }
}
