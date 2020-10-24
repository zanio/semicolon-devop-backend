package com.semicolondevop.suite.repository.developer;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.semicolondevop.suite.model.developer.Developer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 15/10/2020 - 4:38 AM
 * @project com.semicolondevop.suite.repository.developer in ds-suite
 */

@SpringBootTest(properties = "spring.profiles.active=test")
@Sql(scripts={"classpath:/db/development/developer.sql"})
@Slf4j
@Transactional
@ActiveProfiles("test")
class DeveloperRepositoryTest {


    @Autowired
    public DeveloperRepository developerRepositoryImpl;

    Developer testDeveloper;
    Developer testSaver1;
    Developer testSaver2;

//    @BeforeAll
//    void

    @BeforeEach
    void setUp() throws Exception {
        log.info("Test Saver object --> {}", testDeveloper);
        testSaver1= new Developer();
        testSaver1.setId(1);
        testSaver1.setFirstname("Aniefiok");
        testSaver1.setLastname("Akpan");
        testSaver1.setAuthId("fjhdfhdfjhdfjh");;
        testSaver1.setDateJoined(new Date(System.currentTimeMillis()));
        testSaver1.setPhoneNumber("08695656120");
        testSaver1.setImageUrl("https://localhost:2323");
        testSaver1.setUsername("ZANIO");
        developerRepositoryImpl.save(testSaver1);

        log.info("Test Saver object --> {}", testDeveloper);

    }


    @Test
    void findByEmail() {
        Developer developer = developerRepositoryImpl.findByEmail("ZANIO");
        log.info("The devs {}",developerRepositoryImpl.findAll());
        assertThat(developer).isNotNull();
    }


}