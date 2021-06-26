package com.code4unb.TemperatureLINEBot.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

@AllArgsConstructor
@Builder
@Getter
@With
@ToString
public class MeasurementData {
    private final String temperature;

    private final LocalDate date;

    private final TimeConvention convention;

    public enum TimeConvention{
        AM,
        PM;
        public static TimeConvention Now(){
            switch(Calendar.getInstance().get(Calendar.AM_PM)){
                case Calendar.AM:
                    return AM;
                case Calendar.PM:
                    return PM;
            }
            return null;
        }
        public static Optional<TimeConvention> Parse(String text){
            if(text.equalsIgnoreCase("am") || text.contains("前")){
                return Optional.of(AM);
            }else if(text.equalsIgnoreCase("pm")|| text.contains("後")){
                return Optional.of(PM);
            }else{
                return Optional.empty();
            }
        }
    }
}
