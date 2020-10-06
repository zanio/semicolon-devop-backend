package com.semicolondevop.suite.client.developer;


import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.developer.DeveloperLoginDto;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.service.developer.DeveloperService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DeveloperTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Value("${url.github}")
    private String githubUrl;

    @Value("${auth.id}")
    private String authId;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DeveloperRepository developerRepositoryImpl;

    @Autowired
    @Qualifier("user")
    private UserRepository userRepositoryImpl;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    private String getGithubRootUrl(){
        return githubUrl;
    }

    @BeforeEach
    public void http_credentials(){

    }

    @Test
    public void contextLoads() {
        log.info("The Application is running on the following url => {}",getRootUrl());
        log.info("Github Root Url was successfully loaded => {}",getGithubRootUrl());
        assertThat(getRootUrl()).isNotNull();
        assertThat(getGithubRootUrl()).isNotNull();
    }

    @Test
    public void after_user_authenticate_With_github_then_getUserProfile_and_save_to_db() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("Pizzly-Auth-Id", authId);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<GithubDeveloperDao> response = null;

        try {
        response = restTemplate.exchange(getGithubRootUrl() + "user",
                    HttpMethod.GET, entity, GithubDeveloperDao.class);
            if(Objects.requireNonNull(response.getBody()).getLogin() != null){
                GithubDeveloperDao githubDeveloperDao = response.getBody();
                githubDeveloperDao.setPassword(passwordEncoder.encode("MasterCraft"));
                githubDeveloperDao.setPhoneNUmber("08167124344");
                githubDeveloperDao.setAuthId(authId);
                ApplicationUser applicationUser = new ApplicationUser(githubDeveloperDao);
                userRepositoryImpl.save(applicationUser);
                Developer developer = new Developer(githubDeveloperDao);
                developer.setApplicationUser(applicationUser);
              Developer developer1 =  developerRepositoryImpl.save(developer);
              log.info("THE USER HAS BEEN SAVED IN THE DB: {}",developer1);
            }
        } catch (Exception e){
            log.error("The cause of the error is {}", e.getCause().getLocalizedMessage());
            throw new Exception(e.getCause());
        }
       ;
        log.info("The avartar url is {}", Objects.requireNonNull(response.getBody()).getAvatar_url() );


        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAvatar_url()).isEqualTo("https://avatars1.githubusercontent.com/u/38135488?v=4");
    }

    @Test
    public void testGetUserById() {
//  Using Webflux method
        WebClient client3 = WebClient
                .builder()
                .baseUrl(getGithubRootUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Pizzly-Auth-Id", authId)
                .build();
        Flux<GithubDeveloperDao> githubDeveloperDaoFlux =  client3.get()
                .uri("/user")
                .retrieve()
                .bodyToFlux(GithubDeveloperDao.class);

        Objects.requireNonNull(Objects.requireNonNull(githubDeveloperDaoFlux.buffer()).blockFirst()).forEach(e->{
            log.info("the repos,{}",e.getName());
        });

    }

    @Test
    public void it_should_login_user_to_the_application(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        DeveloperLoginDto developerLoginDto = new DeveloperLoginDto("zanio", "MasterCraft");
        HttpEntity<String> entity = new HttpEntity<String>(developerLoginDto.toString(), headers);
        log.info("The method tostring {}", entity);

        ResponseEntity<String> response = null;
      response=   restTemplate.exchange(getRootUrl() + "user",
                HttpMethod.GET, entity, String.class);
    }



}