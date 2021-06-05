package com.code4unb.TemperatureLINEBot;

import com.code4unb.TemperatureLINEBot.message.MessageHandler;
import com.code4unb.TemperatureLINEBot.message.MessageHandlerBase;
import com.code4unb.TemperatureLINEBot.message.ReceivedMessage;
import com.code4unb.TemperatureLINEBot.message.SessionMessageHandler;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

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
