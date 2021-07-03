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
    @Builder.Default
    private Optional<List<Message>> result = Optional.empty();

    private boolean succeed;
}
