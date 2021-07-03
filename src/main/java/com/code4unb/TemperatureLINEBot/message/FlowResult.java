package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Optional;

@Getter
@Builder()
@AllArgsConstructor
public class FlowResult {
    public static final FlowResult EMPTY_SUCCEED = FlowResult.builder().succeed(true).build();

    public  static final FlowResult EMPTY_FAILED = FlowResult.builder().succeed(false).build();

    @Singular("singletonResult")
    private List<Message> result;

    private boolean succeed;

    public Optional<List<Message>> getResult(){
        if(result == null || result.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(result);
        }
    }
}
