package com.code4unb.TemperatureLINEBot.model;

import com.linecorp.bot.model.event.source.Source;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Reply<T> {
    Instant timeStamp;

    Source source;

    T content;
}
