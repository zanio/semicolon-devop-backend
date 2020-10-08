package com.semicolondevop.suite.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 11:35 AM
 * @project com.semicolondevop.suite.util in ds-suite
 */

@SpringBootTest
@Slf4j
class GitHubUtilTest {

    private final KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
    private final SecretKey myDesKey = keygenerator.generateKey();

    GitHubUtilTest() throws NoSuchAlgorithmException {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }






}