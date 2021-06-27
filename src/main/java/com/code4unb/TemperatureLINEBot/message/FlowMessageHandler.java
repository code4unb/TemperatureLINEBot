package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.model.PostbackReply;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class FlowMessageHandler extends MessageHandlerBase{

    protected final List<Flow> flow;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    protected SessionManager sessionManager;

    public static final String ID_MESSAGE_FLOW = "message_flow";

    public static final String ID_HANDLER_TYPE = "handler_type";

    public FlowMessageHandler(String keyPhrase, String... aliases) {
        super(keyPhrase, aliases);
        flow = initFlows();
    }

    protected abstract List<Flow> initFlows();

    protected abstract List<Message> handleActivateMessage(MessageReply message);

    @Override
    public final List<Message> handleMessage(MessageReply message){
        String currentUserId = message.getSource().getUserId();

        Optional<Session> sessionOptional = sessionManager.findSession(currentUserId);
        if(!sessionOptional.isPresent()) {
            Session newSession = new Session();
            newSession.addData(ID_MESSAGE_FLOW, new MessageFlow(flow));
            newSession.addData(ID_HANDLER_TYPE, applicationContext.getBeanNamesForType(this.getClass())[0]);
            sessionManager.addSession(currentUserId, newSession);

            List<Message> result = new ArrayList<>();
            List<Message> handleResult = handleActivateMessage(message);
            if (handleResult != null) {
                result.addAll(handleResult);
            }
            if(sessionManager.findSession(currentUserId).isPresent())((MessageFlow) sessionManager.findSession(currentUserId).get().getData(ID_MESSAGE_FLOW)).getCurrentFlow().postHandle().ifPresent(result::addAll);
            if (result.size() == 0) return null;
            return result;
        }else{
            Session session = sessionOptional.get();
            if (!sessionManager.hasActiveSession(currentUserId)) {
                sessionManager.removeSession(currentUserId);
                return Collections.singletonList(TextMessage.builder()
                        .text("有効期限が切れています。最初からやり直してください。")
                        .build());
            } else {
                session.refresh();

                if (message.getKeyPhrase().equalsIgnoreCase("終了")) {
                    sessionManager.removeSession(currentUserId);
                    return Collections.singletonList(TextMessage.builder()
                            .text("セッションを終了します。")
                            .build());
                }
                List<Message> result = ((MessageFlow) session.getData(ID_MESSAGE_FLOW)).handleNextFlow(message);
                if (((MessageFlow) session.getData(ID_MESSAGE_FLOW)).isCompleted()) {
                    sessionManager.removeSession(currentUserId);
                }

                return result;
            }
        }
    }

    public final List<Message> onPostback(PostbackReply reply){
        Optional<Session> sessionOptional = sessionManager.findSession(reply.getSource().getUserId());
        if(sessionOptional.isPresent()){
            Session session = sessionOptional.get();
            session.refresh();

            List<Message> result = ((MessageFlow)session.getData(ID_MESSAGE_FLOW)).handleNextFlow(reply);
            if(((MessageFlow)session.getData(ID_MESSAGE_FLOW)).isCompleted()) {
                sessionManager.removeSession(reply.getSource().getUserId());
            }

            return result;
        }
        return null;
    }
}
