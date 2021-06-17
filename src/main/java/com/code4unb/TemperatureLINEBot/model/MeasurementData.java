package com.code4unb.TemperatureLINEBot.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Calendar;

@Builder
@Getter
public class MeasurementData {
    private String temperature;

    private LocalDate date;

    private TimeConvention convention;

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
        public static TimeConvention Parse(String text){
            if(text.equalsIgnoreCase("am") || text.contains("前")){
                return AM;
            }else if(text.equalsIgnoreCase("pm")|| text.contains("後")){
                return PM;
            }else{
                return null;
            }
        }
    }
}
