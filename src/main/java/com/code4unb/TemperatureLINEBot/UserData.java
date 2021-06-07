package com.code4unb.TemperatureLINEBot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UserData {
    @Getter
    private Grades Grade;

    @Getter
    private int Class_;

    @Getter
    private int Number;

    @Getter
    private String FirstName;

    @Getter
    private String LastName;

    @Override
    public String toString(){
        return Grade.ordinal()+1+"年"+Class_+"組"+Number+"番"+LastName+" "+FirstName;
    }

    public enum Grades {
        First,
        Second,
        Third;
    }
}
