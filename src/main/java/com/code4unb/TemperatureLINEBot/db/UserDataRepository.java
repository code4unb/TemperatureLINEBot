package com.code4unb.TemperatureLINEBot.db;

import com.code4unb.TemperatureLINEBot.db.entity.UserData;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDataRepository extends CrudRepository<UserData,String> {
    @Query("SELECT * FROM user_data WHERE line_id=:lineID")
    Optional<UserData> findByLineID(@Param("lineID") String lineID);

    default void save(String lineId, UserData.Grades grade, int class_, int number, String firstName, String lastName){
        save(new UserData(lineId, grade, class_, number, firstName, lastName));
    }
}
