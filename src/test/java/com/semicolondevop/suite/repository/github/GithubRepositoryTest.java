package com.semicolondevop.suite.repository.github;

import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.repository.Repository;
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
    @Test
    void findAll() {
        List<Repository> developers = githubRepositoryImpl.findAll();
        assertThat(developers.size()).isEqualTo(2);
    }
}