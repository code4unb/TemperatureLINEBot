package com.code4unb.TemperatureLINEBot;

import com.code4unb.TemperatureLINEBot.message.*;
import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@LineMessageHandler
@Controller
public class MessageController {
    @Autowired(required = false)
    Map<String,MessageHandlerBase> handlers;

    @Autowired(required = false)
    Set<FlowMessageHandler> flowHandlers;

    @Autowired
    SessionManager sessionManager;

    @EventMapping
    public List<Message> handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        ReceivedMessage message = ReceivedMessage.Build(event);
        log.info("Message received :"+message.getKeyPhrase());

        Optional<Session> session = sessionManager.findSession(message.getSource().getUserId());

        if(session.isPresent() && !session.get().isExpired() && !((MessageFlow)session.get().getData(FlowMessageHandler.ID_MESSAGE_FLOW)).isCompleted()){
            return handlers.get((String)session.get().getData(FlowMessageHandler.ID_HANDLER_TYPE)).handleMessage(message);
        }

        for(MessageHandlerBase handler: handlers.values()){
            if(handler.shouldHandle(message.getKeyPhrase())){
                return handler.handleMessage(message);
            }
        }
        return null;
    }

    @EventMapping
    public List<Message> handlePostBackEvent(PostbackEvent event){
        log.info("received post back event");
        Optional<FlowMessageHandler> handler =  flowHandlers.stream()
                .filter(x->x.shouldHandlePostback(event.getSource().getUserId(),event.getPostbackContent().getData()))
                .findFirst();
        return handler.map(flowMessageHandler -> flowMessageHandler.onPostback(new ReceivedMessage(event.getTimestamp(),event.getSource(),null,null,null), event.getPostbackContent())).orElse(null);
    }
}
