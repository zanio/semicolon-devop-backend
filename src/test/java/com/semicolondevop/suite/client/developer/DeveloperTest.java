package com.semicolondevop.suite.client.developer;


import com.cdancy.jenkins.rest.JenkinsClient;
import com.semicolondevop.suite.dao.webhook.WebhookResponse;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.*;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.model.repository.dao.post.RepoResponsePush;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.github.GithubRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.service.json.JsonObject;
import com.semicolondevop.suite.util.github.GithubService;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.google.common.base.Throwables;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.util.Strings2.toStringAndClose;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = "spring.profiles.active=test")
@Slf4j
@Sql(scripts={"classpath:/db/development/developer.sql", "classpath:/db/development/github_repository.sql"})
@ActiveProfiles("test")
@Transactional
public class DeveloperTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JsonObject jsonObject;

    @LocalServerPort
    private int port;

    @Value("${url.github}")
    private String githubUrl;

    @Value("${github.auth.url}")
    private String githubAuthUrl;

    @Value("${jenkins.auth}")
    private String jenkinsCredentials;

    @Value("${jenkins.url}")
    private String jenkinsUrl;

    @Value("${auth.id}")
    private String authId;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JenkinsClient client;

    @Autowired
    private GithubRepository githubRepositoryImpl;

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

       if (developerRepositoryImpl.findByEmail("zanio")==null){
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
       }else{
           log.info("User already exist in the database");
       }



    }


    @Test
    public void it_should_create_repo_from_a_template() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("Pizzly-Auth-Id", authId);
        String name = "name";
        HttpEntity<String> entity = new HttpEntity<String>(name, headers);

        ResponseEntity<String> response = null;
        response = restTemplate.exchange(getGithubRootUrl() + "repos/zanio/senicolon-devop-backend/generate",
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