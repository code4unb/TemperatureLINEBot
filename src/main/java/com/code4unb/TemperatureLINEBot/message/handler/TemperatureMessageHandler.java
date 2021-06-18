package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.FlowMessageHandler;
import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
            return checkFloat(phrase);
        }
    }

    @Override
    protected List<Message> handleActivateMessage(ReceivedMessage message) {
        if(checkFloat(message.getKeyPhrase())){
            close();
            float input = Float.parseFloat(message.getKeyPhrase());
            if(33f <= input && input < 43f){
                MeasurementData data =  MeasurementData.builder()
                        .temperature(String.format("%.1f",input))
                        .date(LocalDate.now())
                        .convention(MeasurementData.TimeConvention.Now())
                        .build();
                return Collections.singletonList(TextMessage.builder().text(data.toString()).build());
            }else{
                return Collections.singletonList(TextMessage.builder().text("不正な入力値 "+input+" です。33℃から43℃の範囲内で入力してください。").build());
            }
        }else{
            return Collections.singletonList(TextMessage.builder().text("体温を入力してください。").build());
        }
    }

    @Override
    protected List<Message> handleSessionMessage(ReceivedMessage message) {
        return null;
    }

    private boolean checkFloat(String text){
        try{
            Float.parseFloat(text);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
