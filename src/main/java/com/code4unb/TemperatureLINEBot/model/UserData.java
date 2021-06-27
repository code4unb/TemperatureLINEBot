package com.code4unb.TemperatureLINEBot.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"LineID"})
@Getter
public class UserData {
    private final String LineID;

    private final Grades Grade;

    private final int Class_;

    private final int Number;

    private final String FirstName;

    private final String LastName;

    public ClassRoom getClassRoom(){
        return new ClassRoom(Grade, Class_);
    }

    public String getName(){
        return LastName+" "+FirstName;
    }

    @Override
    public String toString(){
        return Grade.ordinal()+1+"年"+Class_+"組"+Number+"番"+LastName+" "+FirstName;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ClassRoom{
        private final Grades grade;

        private final int class_;
    }

    public enum Grades {
        First,
        Second,
        Third;
    }
}
