package com.code4unb.TemperatureLINEBot.db.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_data")
@Getter
@EqualsAndHashCode(of = {"id"})
public class UserData {
    @Column("id")
    @Id
    @With
    private final int id;

    //Encrypt
    @Column("line_id")
    private final String lineId;

    @Column("grade")
    private final Grades grade;

    @Column("class")
    private final int class_;

    @Column("number")
    private final int number;

    //Encrypt
    @Column("first_name")
    private final String firstName;

    //Encrypt
    @Column("last_name")
    private final String lastName;

    public ClassRoom getClassRoom(){
        return new ClassRoom(grade, class_);
    }

    public String getName(){
        return lastName+" "+firstName;
    }

    @PersistenceConstructor
    public UserData(int id, String lineId, Grades grade, int class_, int number, String firstName, String lastName){
        this.id = id;
        this.lineId = lineId;
        this.grade = grade;
        this.class_ = class_;
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserData(String lineId, Grades grade, int class_, int number, String firstName, String lastName){
        this(0,lineId,grade,class_,number,firstName,lastName);
    }

    @Override
    public String toString(){
        return grade.ordinal()+1+"年"+class_+"組"+number+"番"+lastName+" "+firstName;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ClassRoom{
        private final Grades grade;

        private final int class_;

        @Override
        public String toString(){
            return grade.ordinal()+1+"-"+class_;
        }
    }

    public enum Grades {
        First,
        Second,
        Third;
    }
}
