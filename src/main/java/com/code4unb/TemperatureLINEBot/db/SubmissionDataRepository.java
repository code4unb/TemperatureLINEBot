package com.code4unb.TemperatureLINEBot.db;

import com.code4unb.TemperatureLINEBot.db.entity.SubmissionData;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;

public interface SubmissionDataRepository extends CrudRepository<SubmissionData,String> {
    default void save(int userId, Timestamp timestamp, String query, SubmissionData.CommandType commandType, String states){
        save(new SubmissionData(userId,timestamp,query,commandType,states));
    }
}
