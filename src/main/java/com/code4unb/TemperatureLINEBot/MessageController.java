package com.code4unb.TemperatureLINEBot;

import com.code4unb.TemperatureLINEBot.message.*;
import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.model.PostbackReply;
import com.code4unb.TemperatureLINEBot.util.FlexMessages;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

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
        MessageReply message = MessageReply.Build(event);

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
        PostbackReply reply = PostbackReply.Build(event);
        log.info(String.format("Postback received '%s' from '%s'",reply.getData(),reply.getSource().getUserId()));

        Optional<Session> session = sessionManager.findSession(reply.getSource().getUserId());

        if(session.isPresent() && !session.get().isExpired() && !((MessageFlow)session.get().getData(FlowMessageHandler.ID_MESSAGE_FLOW)).isCompleted()){
            MessageHandlerBase handler =  handlers.get((String)session.get().getData(FlowMessageHandler.ID_HANDLER_TYPE));
            if(handler instanceof FlowMessageHandler){
                return ((FlowMessageHandler)handler).onPostback(reply);
            }
        }
        return null;
    }

    @EventMapping
    public List<Message> followEvent(FollowEvent event){
        log.info(String.format("New follower '%s'",event.getSource().getUserId()));
        return Arrays.asList(
                FlexMessage.builder().altText("welcome").contents(FlexMessages.LoadContainerFromJsonFile("welcome")).build()
        );
    }

    @EventMapping
    public void handleDefaultEvent(Event event){
        return;
    }
}
