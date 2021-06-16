package com.code4unb.TemperatureLINEBot.db;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDataRepository extends CrudRepository<UserDataEntity,String> {
    @Query("SELECT * FROM user_data WHERE LINE_ID=:lineID")
    Optional<UserDataEntity> findByLineID(@Param("lineID") String lineID);
}
