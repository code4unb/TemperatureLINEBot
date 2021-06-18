package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class FlowResult {
    private Optional<List<Message>> result;

    private boolean succeed;
}
