package com.code4unb.TemperatureLINEBot;

import com.code4unb.TemperatureLINEBot.db.UserDataEntity;
import com.code4unb.TemperatureLINEBot.db.UserDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.util.Assert;

import java.util.Optional;

@DataJdbcTest
public class UserDataRepositoryTest {
    @Autowired
    private UserDataRepository userDataRepository;

    @Test
    void test(){
        UserDataEntity entity = new UserDataEntity(null,new UserData("lineid_here", UserData.Grades.First,1,1,"John","Smith"));
        UserDataEntity saved = userDataRepository.save(entity);
        Assertions.assertNotNull(saved.getID());
        Optional<UserDataEntity> found = userDataRepository.findById(saved.getID());
        Assertions.assertTrue(found.isPresent());
    }
}
