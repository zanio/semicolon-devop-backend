package com.semicolondevop.suite.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.util.github.GitHubUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 11:35 AM
 * @project com.semicolondevop.suite.util in ds-suite
 */

//@SpringBootTest
@Slf4j
class GitHubUtilTest {

    private final KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
    private final SecretKey myDesKey = keygenerator.generateKey();

    String json = "[{\"name\": \"Java\", \"description\": \"Java is a class-based, object-oriented programming language.\"},{\"name\": \"Python\", \"description\": \"Python is an interpreted, high-level and general-purpose programming language.\"}, {\"name\": \"JS\", \"description\": \"JS is a programming language that conforms to the ECMAScript specification.\"}]";

    GitHubUtilTest() throws NoSuchAlgorithmException {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void it_should_convert_value() throws JsonProcessingException {
        List<GithubRepoResponseDao> responseDaoList = GitHubUtil.listAllRepository(json);
        log.info("The github response dao{}",responseDaoList);
    }




}