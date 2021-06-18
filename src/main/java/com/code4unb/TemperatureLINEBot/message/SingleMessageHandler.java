package com.code4unb.TemperatureLINEBot.message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SingleMessageHandler extends MessageHandlerBase{
    public SingleMessageHandler(String keyPhrase, String... aliases) {
        super(keyPhrase, aliases);
    }
}
