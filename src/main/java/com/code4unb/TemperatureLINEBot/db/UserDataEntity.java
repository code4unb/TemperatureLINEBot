package com.code4unb.TemperatureLINEBot.db;

import com.code4unb.TemperatureLINEBot.UserData;
import com.code4unb.TemperatureLINEBot.UserData.Grades;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("USER_DATA")
@EqualsAndHashCode(of = {"ID"})
public class UserDataEntity {
    @Column("id")
    @Id
    @Wither
    private final String ID;

    //Encrypt
    @Column("LINE_ID")
    private final String LineID;

    @Column("GRADE")
    private final Grades Grade;

    @Column("CLASS")
    private final int Class_;

    @Column("NUMBER")
    private int Number;

    //Encrypt
    @Column("FIRST_NAME")
    private final String FirstName;

    //Encrypt
    @Column("LAST_NAME")
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
