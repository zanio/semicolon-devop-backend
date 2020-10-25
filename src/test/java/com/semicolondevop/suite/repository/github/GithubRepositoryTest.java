package com.semicolondevop.suite.repository.github;

import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.model.techStack.TechStack;
import com.semicolondevop.suite.repository.app.AppRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 24/10/2020 - 11:11 PM
 * @project com.semicolondevop.suite.repository.github in ds-suite
 */

@SpringBootTest(properties = "spring.profiles.active=test")
@Sql(scripts={"classpath:/db/development/github_repository.sql"})
@Slf4j
@Transactional
@ActiveProfiles("test")
class GithubRepositoryTest {
    @Autowired
    private GithubRepository githubRepositoryImpl;

    @Autowired
    private AppRepository appRepositoryImpl;
    @Test
    void findAll() {
        List<Repository> developers = githubRepositoryImpl.findAll();
        assertThat(developers.size()).isEqualTo(2);
    }
    @Test
    void CreateRepository() throws Exception {
        App app = appRepositoryImpl.findById(1).orElseThrow(()->new Exception("Id not find"));

        Repository repository = new Repository(app);
        repository.setFullName("zanio/cod");
        repository.setId(123);
        repository.setRepoLink("https://google.com");
        repository.setDateCreated(new Date());


        Repository repository1 = githubRepositoryImpl.save(repository);
        log.info("THE REPOSITORY IS AS FOLLOW {}",repository1);

        assertThat(repository1).isNotNull();


    }
}