package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.SingleMessageHandler;
import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.util.FlexJson;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Component
public class AboutMessageHandler extends SingleMessageHandler {
    @Autowired
    BuildProperties buildProperties;

    public AboutMessageHandler(){
        super("About","アプリについて","version","バージョン");
    }

    @Override
    public List<Message> handleMessage(MessageReply message) {
        String json = FlexJson.LoadMessageJson("about");
        json = String.format(json, buildProperties.getName(), buildProperties.getVersion(), buildProperties.getTime().atZone(ZoneId.systemDefault()).toString());
        return Collections.singletonList(new FlexMessage("version:"+buildProperties.getVersion(),FlexJson.CreateFlexContainer(json)));
    }
}
