package com.code4unb.TemperatureLINEBot.db.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table("submission_data")
@Getter
@EqualsAndHashCode(of = {"id"})
public class SubmissionData {
    @Column("submission_id")
    @Id
    @With
    private final int id;

    @Column("user_id")
    int userId;

    @Column("timestamp")
    Timestamp timestamp;

    @Column("query")
    String query;

    @Column("command_type")
    CommandType commandType;

    @Column("states")
    String states;

    @PersistenceConstructor
    public SubmissionData(int id, int userId, Timestamp timestamp, String query, CommandType commandType, String states){
        this.id = id;
        this.userId = userId;
        this.timestamp = timestamp;
        this.query = query;
        this.commandType = commandType;
        this.states = states;
    }

    public SubmissionData(int userId, Timestamp timestamp, String query, CommandType commandType, String states){
        this(0,userId,timestamp,query,commandType,states);
    }

    public enum CommandType{
        SIMPLE,
        NORMAL
    }
}
