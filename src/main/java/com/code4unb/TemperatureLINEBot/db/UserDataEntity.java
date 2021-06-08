package com.code4unb.TemperatureLINEBot.db;

import com.code4unb.TemperatureLINEBot.UserData;
import com.code4unb.TemperatureLINEBot.UserData.Grades;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class UserDataEntity {
    @Column("id")
    @Id
    @With
    private final String ID;

    //Encrypt
    @Column("line_id")
    private final String LineID;

    @Column("grade")
    private final Grades Grade;

    @Column("class")
    private final int Class_;

    @Column("number")
    private int Number;

    //Encrypt
    @Column("first_name")
    private final String FirstName;

    //Encrypt
    @Column("last_name")
    private final String LastName;

    public UserDataEntity(String id,String lineID,Grades grade,int class_,int number,String firstName,String lastName){
        ID = id;
        LineID = lineID;
        Grade = grade;
        Class_ = class_;
        Number = number;
        FirstName = firstName;
        LastName = lastName;
    }
    public UserDataEntity(String id,UserData data){
        this(id,data.getLineID(),data.getGrade(),data.getClass_(),data.getNumber(),data.getFirstName(),data.getLastName());
    }
    public UserData toUserData(){
        return new UserData(getLineID(),getGrade(),getClass_(),getNumber(),getFirstName(),getLastName());
    }
}
