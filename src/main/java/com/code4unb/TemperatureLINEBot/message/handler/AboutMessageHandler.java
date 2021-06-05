package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.TemperatureLineBotApplication;
import com.code4unb.TemperatureLINEBot.message.Handler;
import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Handler
public class AboutMessageHandler extends MessageHandler {
    public AboutMessageHandler(){
        super("About","アプリについて","version","バージョン");
    }
    @Override
    public Message HandleMessage(ReceivedMessage message) {
        return new TextMessage(TemperatureLineBotApplication.BuildProperties.toString());
    }
}
