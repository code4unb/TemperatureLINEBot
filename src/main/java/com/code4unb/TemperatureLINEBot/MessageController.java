package com.code4unb.TemperatureLINEBot;

import com.code4unb.TemperatureLINEBot.message.MessageHandlerBase;
import com.code4unb.TemperatureLINEBot.message.SessionMessageHandler;
import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Slf4j
@LineMessageHandler
@Controller
public class MessageController {
    @Autowired(required = false)
    Set<MessageHandlerBase> Handlers;

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        ReceivedMessage message = ReceivedMessage.Build(event);
        log.info("Message received :"+message.getKeyPhrase());
        if(SessionMessageHandler.getCurrentSession() != null && SessionMessageHandler.getCurrentSession().isOpen()){
            return SessionMessageHandler.getCurrentSession().handleMessage(message);
        }
        for(MessageHandlerBase handler: Handlers){
            if(handler.shouldHandle(message.getKeyPhrase())){
                return handler.handleMessage(message);
            }
        }
        return null;
    }
}
