package com.semicolondevop.suite.repository.app;

import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.techStack.TechStack;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.teckStack.TechStackRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 24/10/2020 - 11:10 PM
 * @project com.semicolondevop.suite.repository.app in ds-suite
 */
@SpringBootTest(properties = "spring.profiles.active=test")
@Sql(scripts={"classpath:/db/development/app.sql"})
@Slf4j
@Transactional
@ActiveProfiles("test")
class AppRepositoryTest {
    @Autowired
    private AppRepository appRepositoryImpl;

    @Autowired
    private DeveloperRepository developerRepositoryImpl;

    @Autowired
    private TechStackRepository techStackRepositoryImpl;

    @Test
    void findAll() {
        List<App> developers = appRepositoryImpl.findAll();
        assertThat(developers.size()).isEqualTo(2);
    }
    @Test
    void createApp() throws Exception {
        App app = new App();
        app.setId(10);
        app.setDomain("https://google.com");
        app.setDescription("This is the description");

        Developer developer = developerRepositoryImpl.findById(46).orElseThrow(()->new Exception("Id not find"));
        app.setDeveloper(developer);

        TechStack techStack = techStackRepositoryImpl.findById(1).orElseThrow(()->new Exception("Id not find"));

        app.setTechStack(techStack);

        assertThat(appRepositoryImpl.save(app)).isNotNull();


    }
}