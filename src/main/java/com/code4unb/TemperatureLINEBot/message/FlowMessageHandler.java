package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FlowMessageHandler extends MessageHandlerBase{

    protected MessageFlow flow;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private long expirationSecond = 600;

    @Getter
    private Instant createdDate;

    @Getter
    private Instant refreshedDate;

    @Getter
    private boolean open;

    @Getter
    private static FlowMessageHandler CurrentSession;

    public FlowMessageHandler(String keyPhrase, String... aliases) {
        super(keyPhrase, aliases);
        flow = new MessageFlow(initFlows());
    }

    protected abstract List<Flow> initFlows();

    protected abstract List<Message> handleActivateMessage(ReceivedMessage message);

    public boolean shouldHandle(){
        if(flow.isCompleted() || !isOpen()){
            close();
            return false;
        }else{
            return true;
        }
    }

    @Override
    public final List<Message> handleMessage(ReceivedMessage message){
        if(isOpen()){
            if(isExpired()){
                close();
                return Collections.singletonList(TextMessage.builder()
                        .text("有効期限が切れています。最初からやり直してください。")
                        .build());
            }
            refresh();
            if(message.getKeyPhrase().equalsIgnoreCase("終了")){
                close();
                return Collections.singletonList(TextMessage.builder()
                        .text("セッションを終了します。")
                        .build());
            }

            return flow.handleNextFlow(message);
        }else{
            flow.init();
            open();
            List<Message> result = new ArrayList<Message>();
            List<Message> handleResult = handleActivateMessage(message);
            if(handleResult!=null){
                result.addAll(handleResult);
            }
            if(isOpen()) flow.getCurrentFlow().postHandle().ifPresent(y->result.addAll(y));
            if(result.size()==0)return null;
            return result;
        }
    }

    public boolean isExpired(){
        return refreshedDate.plusSeconds(expirationSecond).isBefore(Instant.now());
    }

    protected final void open(){
        createdDate = Instant.now();
        refreshedDate = createdDate;
        open = true;
        FlowMessageHandler.CurrentSession = this;
    }

    protected final void refresh(){
        refreshedDate = Instant.now();
    }

    protected void close(){
        open = false;
    }
}
