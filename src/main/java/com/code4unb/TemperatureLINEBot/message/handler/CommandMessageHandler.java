package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.Handler;
import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@Handler
public class CommandMessageHandler extends MessageHandler {
    public CommandMessageHandler() {
        super("Commands","コマンド");
    }

    @Override
    public Message HandleMessage(ReceivedMessage message) {
        String text = String.join(",",MessageHandler.getHandlerInstances().values().stream().map(x->x.getKeyPhrase()).toArray(String[]::new));
        return TextMessage.builder().text(text).build();
    }
}
