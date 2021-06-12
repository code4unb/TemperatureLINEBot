package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.MessageHandlerBase;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CommandMessageHandler extends MessageHandler {
    @Autowired(required = false)
    Set<MessageHandlerBase> Handlers;

    public CommandMessageHandler() {
        super("Commands","コマンド");
    }

    @Override
    public Message handleMessage(ReceivedMessage message) {
        String text = String.join(",",Handlers.stream().map(x->x.getKeyPhrase()).toArray(String[]::new));
        return TextMessage.builder().text(text).build();
    }
}
