package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.Flow;
import com.code4unb.TemperatureLINEBot.message.FlowMessageHandler;
import com.code4unb.TemperatureLINEBot.message.FlowResult;
import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class TemperatureMessageHandler extends FlowMessageHandler {
    public TemperatureMessageHandler(){
        super("入力","体温入力","体温","検温入力","検温");
        setHidden(true);
    }

    @Override
    public boolean shouldHandle(String phrase){
        if(super.shouldHandle(phrase)){
            return true;
        }else{
            return parseFloat(phrase).isPresent();
        }
    }

    @Override
    protected List<Flow> initFlows() {
        return Arrays.asList(
                new Flow() {
                    @Override
                    public Optional<List<Message>> postHandle() {
                        return Optional.of(Collections.singletonList(TextMessage.builder().text("体温を入力してください。").build()));
                    }

                    @Override
                    public FlowResult handle(ReceivedMessage message) {
                        if(parseFloat(message.getKeyPhrase()).isPresent()){
                            return new FlowResult(Optional.empty(),true);
                        }else{
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。体温は33℃から43℃の範囲内で入力してください。").build())),false);
                        }
                    }
                },
                new Flow(){

                    @Override
                    public Optional<List<Message>> postHandle() {
                        return Optional.of(Collections.singletonList(TextMessage.builder().text("午前/午後(AM/PM)を入力してください。").build()));
                    }

                    @Override
                    public FlowResult handle(ReceivedMessage message) {
                        return new FlowResult(Optional.empty(),true);
                    }
                }
        );
    }

    @Override
    protected List<Message> handleActivateMessage(ReceivedMessage message) {
        Optional<Float> parsed = parseFloat(message.getKeyPhrase());
        if (parsed.isPresent()) {
            close();
            float input = parsed.get();
            if (33f <= input && input < 43f) {
                MeasurementData data = MeasurementData.builder()
                        .temperature(String.format("%.1f", input))
                        .date(LocalDate.now())
                        .convention(MeasurementData.TimeConvention.Now())
                        .build();
                return Collections.singletonList(TextMessage.builder().text(data.toString()).build());
            } else {
                return Collections.singletonList(TextMessage.builder().text("不正な入力値 " + input + " です。体温は33℃から43℃の範囲内で入力してください。").build());
            }
        } else {
            return null;
        }
    }

    private Optional<Float> parseFloat(String text){
        try{
            return Optional.of(Float.parseFloat(text));
        }catch(Exception e){
            return Optional.empty();
        }
    }
}
