package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.TemperatureLineBotApplication;
import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.code4unb.TemperatureLINEBot.util.FlexJson;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class AboutMessageHandler extends MessageHandler {
    @Autowired
    BuildProperties buildProperties;

    public AboutMessageHandler(){
        super("About","アプリについて","version","バージョン");
    }
    @Override
    public Message HandleMessage(ReceivedMessage message) {
        String json = FlexJson.LoadMessageJson("about");
        json = String.format(json, buildProperties.getName(), buildProperties.getVersion(), buildProperties.getTime().atZone(ZoneId.systemDefault()).toString());
        return new FlexMessage("version:"+buildProperties.getVersion(),FlexJson.CreateFlexContainer(json));
    }
}
