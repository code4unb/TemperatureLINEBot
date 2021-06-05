package com.code4unb.TemperatureLINEBot;

import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.MessageHandlerBase;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.code4unb.TemperatureLINEBot.message.SessionMessageHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.message.sender.Sender;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@LineMessageHandler
@Controller
public class MessageController {
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        ReceivedMessage message = ReceivedMessage.Build(event);
        log.info("Message received :"+message.getKeyPhrase());
        if(SessionMessageHandler.getCurrentSession() != null && SessionMessageHandler.getCurrentSession().isOpen()){
            return SessionMessageHandler.getCurrentSession().HandleMessage(message);
        }
        for(MessageHandlerBase handler:MessageHandler.getHandlerInstances().values()){
            if(handler.shouldHandle(message.getKeyPhrase())){
                return handler.HandleMessage(message);
            }
        }
        return null;
    }
}
