package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.TemperatureLineBotApplication;
import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class AboutMessageHandler extends MessageHandler {
    public AboutMessageHandler(){
        super("About","アプリについて","version","バージョン");
        MessageHandler.RegisterHandler(AboutMessageHandler.class);
    }
    @Override
    public Message HandleMessage(ReceivedMessage message) {
        return new TextMessage(TemperatureLineBotApplication.getBuildProperties().toString());
    }
}
