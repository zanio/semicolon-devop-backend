package com.semicolondevop.suite.repository.developer;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    @Qualifier("user")
    public UserRepository userRepositoryImpl;


    @Test
    void findByEmail() {
        Developer developer = developerRepositoryImpl.findByEmail("zanio");
        assertThat(developer).isNotNull();
    }

    @Test
    void findAll() {
        List<Developer> developers = developerRepositoryImpl.findAll();
        assertThat(developers.size()).isEqualTo(2);
    }

    @Test
    void createDeveloper(){
        GithubDeveloperDao githubDeveloperDao = new GithubDeveloperDao();
        githubDeveloperDao.setPhoneNumber("09034134521");
        githubDeveloperDao.setAuthId("yreyruy dfhgjhgjh jhgjfh");
        githubDeveloperDao.setAvatar_url("http://localhost:3000/");
        githubDeveloperDao.setPassword("ascdf");
        githubDeveloperDao.setLogin("github_user");
        githubDeveloperDao.setName("GITHUB USER");

        Developer developer = new Developer(githubDeveloperDao);
        ApplicationUser applicationUser = new ApplicationUser(githubDeveloperDao);
        ApplicationUser applicationUser1 = userRepositoryImpl.save(applicationUser);
        developer.setApplicationUser(applicationUser1);
        assertThat(developerRepositoryImpl.save(developer)).isNotNull();

    }

    @Test
    void findByToken(){
        assertThat(developerRepositoryImpl.findByToken("93bf6e70-087e-11eb-94f2-17029daecd99")).isNotNull();
        assertThat(developerRepositoryImpl.findByToken("93bf6e70-087e-11eb-94f2-17029daecd99").getId()).isEqualTo(45);

    }

    @Test
    void should_return_null(){
        assertThat(developerRepositoryImpl.findByToken("93bf6e70-087e-11eb-94f2-17029daecd9f")).isNull();
    }

    @Test
    void should_findDeveloperBy_developer(){
        developerRepositoryImpl.findById(46);
        assertThat(developerRepositoryImpl.findById(46)).isNotNull();
    }

    @Test
    void should_delete_developer(){
        developerRepositoryImpl.deleteById(45);
        Optional.of(assertThat(developerRepositoryImpl.findById(45)).isEmpty());
    }



}