package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.*;

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

    protected abstract List<Message> handleActivateMessage(ReceivedMessage message);

    @Override
    public final List<Message> handleMessage(ReceivedMessage message){
        String currentUserId = message.getSource().getUserId();

        if(!sessionManager.hasSession(currentUserId)) {
            Session newSession = new Session();
            newSession.addData(ID_MESSAGE_FLOW, new MessageFlow(flow));
            newSession.addData(ID_HANDLER_TYPE,applicationContext.getBeanNamesForType(this.getClass())[0]);
            sessionManager.addSession(currentUserId, newSession);

            List<Message> result = new ArrayList<>();
            List<Message> handleResult = handleActivateMessage(message);
            if(handleResult!=null){
                result.addAll(handleResult);
            }
            ((MessageFlow)sessionManager.findSession(currentUserId).get().getData(ID_MESSAGE_FLOW)) .getCurrentFlow().postHandle().ifPresent(result::addAll);
            if(result.size()==0)return null;
            return result;
        }

        Optional<Session> session = sessionManager.findSession(currentUserId);

        if(!sessionManager.hasActiveSession(currentUserId)) {
            sessionManager.removeSession(currentUserId);
            return Collections.singletonList(TextMessage.builder()
                    .text("有効期限が切れています。最初からやり直してください。")
                    .build());
        }else{
            session.get().refresh();

            if(message.getKeyPhrase().equalsIgnoreCase("終了")){
                sessionManager.removeSession(currentUserId);
                return Collections.singletonList(TextMessage.builder()
                        .text("セッションを終了します。")
                        .build());
            }
            List<Message> result = ((MessageFlow)session.get().getData(ID_MESSAGE_FLOW)).handleNextFlow(message);
            if(((MessageFlow)session.get().getData(ID_MESSAGE_FLOW)).isCompleted()) {
                sessionManager.removeSession(currentUserId);
            }
            if(result != null){
                result.stream()
                        .filter(x->x.getQuickReply()!=null)
                        .flatMap(x->x.getQuickReply()
                                .getItems()
                                .stream()
                                .filter(y->(y.getAction() instanceof DatetimePickerAction))
                                .map(y->(DatetimePickerAction)y.getAction())
                                .map(y->y.getData())
                        )
                        .forEach(x->addPostback(message.getSource().getUserId(),x));
            }
            return result;
        }
    }

    protected void addPostback(String line_id,String data){
        log.info("add postback:"+data);
        Session session = sessionManager.findOrCreateSession(line_id);
        if(!session.hasData("postback")){
            session.addData("postback",new HashSet<String>());
        }
        ((Set)session.getData("postback")).add(data);
        sessionManager.addSession(line_id,session);
    }

    public boolean doesHandlePostback(){
        return false;
    }

    public boolean shouldHandlePostback(String line_id,String data){
        if(!doesHandlePostback()) return false;
        Optional<Session> session = sessionManager.findSession(line_id);
        if(session.isPresent() && session.get().hasData("postback") && ((Set)session.get().getData("postback")).contains(data)){
            return true;
        }else{
            return false;
        }
    }

    public final List<Message> onPostback(ReceivedMessage message, PostbackContent content){
        Optional<Session> session = sessionManager.findSession(message.getSource().getUserId());
        session.get().refresh();
        session.get().removeData("postback");

        List<Message> result = handlePostback(message.getSource().getUserId(),content);;
        ((MessageFlow)session.get().getData(ID_MESSAGE_FLOW)).skipThisFlow();
        if(((MessageFlow)session.get().getData(ID_MESSAGE_FLOW)).isCompleted()){
            sessionManager.removeSession(message.getSource().getUserId());
        }
        return result;
    }

    public List<Message> handlePostback(String line_id,PostbackContent content){
        return null;
    }
}
