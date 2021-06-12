package com.code4unb.TemperatureLINEBot.db;

import com.code4unb.TemperatureLINEBot.UserData;
import com.code4unb.TemperatureLINEBot.UserData.Grades;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("USER_DATA")
@Getter
@EqualsAndHashCode(of = {"id"})
public class UserDataEntity {
    @Column("ID")
    @Id
    @With
    private final String id;

    //Encrypt
    @Column("LINE_ID")
    private final String lineId;

    @Column("GRADE")
    private final Grades grade;

    @Column("CLASS")
    private final int class_;

    @Column("NUMBER")
    private final int number;

    //Encrypt
    @Column("FIRST_NAME")
    private final String firstName;

    //Encrypt
    @Column("LAST_NAME")
    private final String lastName;

    @PersistenceConstructor
    public UserDataEntity(String id,String lineId,Grades grade,int class_,int number,String firstName,String lastName){
        this.id = id;
        this.lineId = lineId;
        this.grade = grade;
        this.class_ = class_;
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public UserDataEntity(String id,UserData data){
        this(id,data.getLineID(),data.getGrade(),data.getClass_(),data.getNumber(),data.getFirstName(),data.getLastName());
    }
    public UserDataEntity(UserData data){
        this(null,data);
    }
    public UserData toUserData(){
        return new UserData(getLineId(),getGrade(),getClass_(),getNumber(),getFirstName(),getLastName());
    }
}
