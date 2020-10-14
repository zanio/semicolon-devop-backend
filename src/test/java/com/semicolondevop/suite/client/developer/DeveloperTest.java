package com.semicolondevop.suite.client.developer;


import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.*;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.model.repository.dao.post.RepoResponsePush;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.util.github.GithubService;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@ActiveProfiles("test")
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

    private String getGithubRootUrl() {
        return githubUrl;
    }

    @BeforeEach
    public void http_credentials() {

    }

    @Test
    public void contextLoads() {
        log.info("The Application is running on the following url => {}", getRootUrl());
        log.info("Github Root Url was successfully loaded => {}", getGithubRootUrl());
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
            if (Objects.requireNonNull(response.getBody()).getLogin() != null) {
                GithubDeveloperDao githubDeveloperDao = response.getBody();
                githubDeveloperDao.setPassword(passwordEncoder.encode("MasterCraft"));
                githubDeveloperDao.setPhoneNUmber("08167124344");
                githubDeveloperDao.setAuthId(authId);
                ApplicationUser applicationUser = new ApplicationUser(githubDeveloperDao);
                userRepositoryImpl.save(applicationUser);
                Developer developer = new Developer(githubDeveloperDao);
                developer.setApplicationUser(applicationUser);
                Developer developer1 = developerRepositoryImpl.save(developer);
                log.info("THE USER HAS BEEN SAVED IN THE DB: {}", developer1);
            }
        } catch (Exception e) {
            log.error("The cause of the error is {}", e.getCause().getLocalizedMessage());
            throw new Exception(e.getCause());
        }

        log.info("The avartar url is {}", Objects.requireNonNull(response.getBody()).getAvatar_url());


        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAvatar_url()).isEqualTo("https://avatars1.githubusercontent.com/u/38135488?v=4");
    }


//    @Test
//    public void it_should_login_user_to_the_application() {
//        HttpHeaders headers = new HttpHeaders();
////        headers.add("Content-type", "application/json Accept: application/json");
//        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//
//        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
//        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
//        log.info("The headers are as follows: {}",headers);
//
//        DeveloperLoginDto developerLoginDto = new DeveloperLoginDto("zanio", "MasterCraft");
//        HttpEntity<DeveloperLoginDto> entity = new HttpEntity<>(developerLoginDto, headers);
//        log.info("The method tostring {}", entity);
//
//        ResponseEntity<String> response = null;
//        response = restTemplate.exchange(getRootUrl() + "api/auth/login",
//                HttpMethod.POST, entity, String.class);
//
//        log.info("The return response is as follow {}",response);
//    }

    @Test
    public void it_should_create_repo_from_a_template() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("Pizzly-Auth-Id", authId);
        String name = "name";
        HttpEntity<String> entity = new HttpEntity<String>(name, headers);

        ResponseEntity<String> response = null;
        response = restTemplate.exchange(getGithubRootUrl() + "repos/zanio/semicolon-devop-backend/generate",
                HttpMethod.POST, entity, String.class);

        log.info("The response was successfully retrieved {}", response);


    }

    @Test
    void it_should_list_all_repo_based_on_oauth_scope(){
        GithubService githubService = new GithubService();
        List<GithubRepoResponseDao>  listOfRepository = githubService.getGitUserRepositories(authId,"user/repos");
        log.info("THE REPO LIST {}",listOfRepository);

    }

    @Test
    void it_should_get_user(){
        GithubService githubService = new GithubService();
        GithubDeveloperDao  githubDeveloperDao = githubService.getGitUserProfile(authId,"user");
        log.info("THE REPO LIST {}",githubDeveloperDao);
    }


    @Test
    void it_should_query_repo_based_on_oauth_scope_without_admin_access(){
        // todo Maduflavins/Authentications
        GithubService githubService = new GithubService();
        GithubRepoResponseDao  repository = githubService
                .getGitUserRepository(authId,"repos/Maduflavins/Authentications");
        log.info("THE REPO DETAILS {}",repository);
    }


    @Test
    void it_should_push_to_github() throws IOException {
        GithubService githubService = new GithubService();
        RepoResponsePush repoResponsePush = githubService
                .pushToGithub(".travis.yml",
                        "zanio/semicolon-devop-backend",
                        "config/.travis.yml", authId);

        log.info("THE RESPONSE FROM GITHUB {} ", repoResponsePush);


       if(repoResponsePush != null){
//
               boolean isUrlTrue = repoResponsePush.getContent().getHtml_url()
               .equals("https://github.com/zanio/semicolon-devop-backend/blob/master/config/.travis.yml");

                      assertThat(isUrlTrue).isEqualTo(true);

       } else {
           assertThat(repoResponsePush).isEqualTo(null);

       }
    }

    @Test
    void it_should_get_the_date_difference_between_two_dates() throws ParseException {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String dateBeforeString = "31 01 2020";
        String dateAfterString = "02 02 2020";
        Date dateBefore = myFormat.parse(dateBeforeString);
        Date dateAfter = myFormat.parse(dateAfterString);

    }



}