package com.semicolondevop.suite.repository.teckStack;

import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.model.techStack.TechStack;
import com.semicolondevop.suite.repository.github.GithubRepository;
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
 * @project com.semicolondevop.suite.repository.teckStack in ds-suite
 */
@SpringBootTest(properties = "spring.profiles.active=test")
@Sql(scripts={"classpath:/db/development/tech_stack.sql"})
@Slf4j
@Transactional
@ActiveProfiles("test")
class TechStackRepositoryTest {

    @Autowired
    private TechStackRepository techStackRepositoryImpl;
    @Test
    void findAll() {
        List<TechStack> developers = techStackRepositoryImpl.findAll();
        assertThat(developers.size()).isEqualTo(2);
    }
    @Test
    void createTechStack(){

        TechStack techStack = new TechStack("JAVA");
        techStack.setId(10);
        assertThat(techStackRepositoryImpl.save(techStack)).isNotNull();
    }

}