package com.code4unb.TemperatureLINEBot;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"LineID"})
public class UserData {
    @Getter
    private final String LineID;

    @Getter
    private final Grades Grade;

    @Getter
    private final int Class_;

    @Getter
    private final int Number;

    @Getter
    private final String FirstName;

    @Getter
    private final String LastName;

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
