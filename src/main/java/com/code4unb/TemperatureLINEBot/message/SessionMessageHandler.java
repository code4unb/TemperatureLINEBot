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

    protected abstract Message HandleActivateMessage(ReceivedMessage message);

    protected abstract Message HandleSessionMessage(ReceivedMessage message);

    @Override
    public final Message HandleMessage(ReceivedMessage message){
        if(isOpen()){
            if(isExpired()){
                Close();
                return TextMessage.builder()
                        .text("有効期限が切れています。最初からやり直してください。")
                        .build();
            }
            Refresh();
            if(message.getKeyPhrase().equalsIgnoreCase("終了")){
                Close();
                return TextMessage.builder()
                        .text("セッションを終了します。")
                        .build();
            }
            return HandleSessionMessage(message);
        }else{
            Open();
            return  HandleActivateMessage(message);
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

    protected final void Refresh(){
        refreshedDate = Instant.now();
    }

    protected void Close(){
        open = false;
    }
}
