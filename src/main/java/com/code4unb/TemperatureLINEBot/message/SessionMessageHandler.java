package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public abstract class SessionMessageHandler extends MessageHandlerBase{
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
    private static SessionMessageHandler CurrentSession;

    public SessionMessageHandler(String keyPhrase, String... aliases) {
        super(keyPhrase, aliases);
    }

    protected abstract Message handleActivateMessage(ReceivedMessage message);

    protected abstract Message handleSessionMessage(ReceivedMessage message);

    @Override
    public final Message handleMessage(ReceivedMessage message){
        if(isOpen()){
            if(isExpired()){
                close();
                return TextMessage.builder()
                        .text("有効期限が切れています。最初からやり直してください。")
                        .build();
            }
            refresh();
            if(message.getKeyPhrase().equalsIgnoreCase("終了")){
                close();
                return TextMessage.builder()
                        .text("セッションを終了します。")
                        .build();
            }
            return handleSessionMessage(message);
        }else{
            Open();
            return  handleActivateMessage(message);
        }
    }

    public boolean isExpired(){
        return refreshedDate.plusSeconds(expirationSecond).isBefore(Instant.now());
    }

    protected final void Open(){
        createdDate = Instant.now();
        refreshedDate = createdDate;
        open = true;
        SessionMessageHandler.CurrentSession = this;
    }

    protected final void refresh(){
        refreshedDate = Instant.now();
    }

    protected void close(){
        open = false;
    }
}
