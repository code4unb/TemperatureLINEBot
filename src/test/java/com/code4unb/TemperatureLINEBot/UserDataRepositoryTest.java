package com.code4unb.TemperatureLINEBot;

import com.code4unb.TemperatureLINEBot.db.UserDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
public class UserDataRepositoryTest {
    @Autowired
    private UserDataRepository userDataRepository;

    @Test
    void test(){/*
        UserDataEntity entity = new UserDataEntity(null,new UserData("lineid_here", UserData.Grades.First,1,1,"John","Smith"));
        UserDataEntity saved = userDataRepository.save(entity);
        Assertions.assertNotNull(saved.getId());
        Optional<UserDataEntity> found = userDataRepository.findById(saved.getId());
        Assertions.assertTrue(found.isPresent());*/
    }
}
