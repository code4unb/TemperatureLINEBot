package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FlowResult {
    private List<Message> result;

    private boolean succeed;
}
