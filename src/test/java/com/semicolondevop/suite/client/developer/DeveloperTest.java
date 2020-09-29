package com.semicolondevop.suite.client.developer;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DeveloperTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {
        log.info("The Application is running on the following url => {}",getRootUrl());
        assertThat(getRootUrl()).isNotNull();
    }

    @Test
    public void testGetAllUsers() {
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/users",
//                HttpMethod.GET, entity, String.class);
//
//        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testGetUserById() {
//        User user = restTemplate.getForObject(getRootUrl() + "/users/1", User.class);
//        System.out.println(user.getFirstName());
//        log.info("get user firstname: {}", user.ge);
//        Assert.assertNotNull(user);
    }

    @Test
    public void testCreateUser() {
//        User user = new User();
//        user.setEmail("admin@gmail.com");
//        user.setFirstName("admin");
//        user.setLastName("admin");
//        user.setCreatedBy("admin");
//        user.setUpdatedBy("admin");
//
//        ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/users", user, User.class);
//        Assert.assertNotNull(postResponse);
//        Assert.assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdatePost() {
//        int id = 1;
//        User user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
//        user.setFirstName("admin1");
//        user.setLastName("admin2");
//
//        restTemplate.put(getRootUrl() + "/users/" + id, user);
//
//        User updatedUser = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
//        Assert.assertNotNull(updatedUser);
    }

    @Test
    public void testDeletePost() {
//        int id = 2;
//        User user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
//        Assert.assertNotNull(user);
//
//        restTemplate.delete(getRootUrl() + "/users/" + id);
//
//        try {
//            user = restTemplate.getForObject(getRootUrl() + "/users/" + id, User.class);
//        } catch (final HttpClientErrorException e) {
//            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
//        }
    }
    


}