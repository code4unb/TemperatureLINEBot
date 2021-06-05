package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.TemperatureLineBotApplication;
import com.code4unb.TemperatureLINEBot.message.Handler;
import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.code4unb.TemperatureLINEBot.util.FlexJson;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import org.springframework.boot.info.BuildProperties;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Handler
public class AboutMessageHandler extends MessageHandler {
    public AboutMessageHandler(){
        super("About","アプリについて","version","バージョン");
    }
    @Override
    public Message HandleMessage(ReceivedMessage message) {
        BuildProperties prop = TemperatureLineBotApplication.BuildProperties;
        String json = FlexJson.LoadMessageJson("about");
        json = String.format(json, prop.getName(), prop.getVersion(), prop.getTime().atZone(ZoneId.systemDefault()).toString());
        return new FlexMessage("version:"+prop.getVersion(),FlexJson.CreateFlexContainer(json));
    }
}
