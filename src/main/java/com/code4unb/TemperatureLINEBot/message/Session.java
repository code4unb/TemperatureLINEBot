package com.code4unb.TemperatureLINEBot.message;

import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Session {
    @Getter
    private final long expirationSecond;

    @Getter
    private final Instant createdDate;

    @Getter
    private Instant refreshedDate;

    private final Map<String,Object> data;

    public Session(){
        this(600);
    }

    public Session(long expirationSecond){
        createdDate = Instant.now();
        refreshedDate = Instant.now();
        this.expirationSecond = expirationSecond;
        data = new HashMap<>();
    }

    public void addData(String key,Object value){
        data.put(key,value);
    }

    public Object getData(String key){
        return data.get(key);
    }

    public boolean isExpired(){
        return refreshedDate.plusSeconds(expirationSecond).isBefore(Instant.now());
    }

    public final void refresh(){
        refreshedDate = Instant.now();
    }
}
