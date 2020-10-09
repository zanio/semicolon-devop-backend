package com.semicolondevop.suite.client.developer;


import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.*;
import com.semicolondevop.suite.model.repository.RepoResponsePush;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.service.developer.DeveloperService;
import com.semicolondevop.suite.util.GithubService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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

        log.info("The avartar url is {}", Objects.requireNonNull(response.getBody()).getAvatar_url() );


        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAvatar_url()).isEqualTo("https://avatars1.githubusercontent.com/u/38135488?v=4");
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

    @Test
    public void it_should_create_repo_from_a_template(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add("Pizzly-Auth-Id", authId);
        String name = "name";
        HttpEntity<String> entity = new HttpEntity<String>(name, headers);

        ResponseEntity<String> response = null;
        response = restTemplate.exchange(getGithubRootUrl() + "repos/zanio/semicolon-devop-backend/generate",
                HttpMethod.POST, entity, String.class);

        log.info("The response was successfully retrieved {}",response);



    }

    @Test
    void it_should_push_to_github() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(".travis.yml").getFile());
        if(file.exists()){
//            String t = encryptFile(file);
//            log.info("The file exist {} and it is encrypted",t);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("Pizzly-Auth-Id", authId);
            HttpEntity<String> entity = new HttpEntity<String>(null, headers);


            ResponseEntity<PushToGithubResponse> response = null;
           String link= "repos/zanio/semicolon-devop-backend/contents/.travis.yml";

            try {
                response = restTemplate.exchange(getGithubRootUrl() + link+"?ref=master",
                        HttpMethod.GET, entity, PushToGithubResponse.class);
                String x1d = decoder(response.getBody().getContent());
                String x2d = decoder(encoder(file));

                if(!x1d.equals(x2d)){
                    PushToGithubResponse githubDeveloperDao = response.getBody();
                    PushToGithubDao pushToGithubDao = new PushToGithubDao(encoder(file),
                            "master","update");

                    HttpEntity<PushToGithubDao> entityPost = new HttpEntity<>(pushToGithubDao, headers);
                    String link1= "repos/zanio/semicolon-devop-backend/contents/config/myfile.txt";

                ResponseEntity<String>    response2 = restTemplate.exchange(getGithubRootUrl() + link,
                            HttpMethod.PUT, entityPost, String.class);
//                    log.info("THE UNDECODED FILE IS: {}",githubDeveloperDao.getContent());
                    log.info("File successfully push to github : {}",response2.getBody() );
                } else{
                    log.info("Nothing to update On github, {}");
                }
            } catch (Exception e){
                log.error("The cause of the error is {}", e.getCause().getLocalizedMessage());
                throw new Exception(e.getCause());
            }

        }

    }

    public  String encoder(File file) {
        String base64File = "";
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.encodeBase64String(fileData);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
        }

        return base64File;
    }

    public  String decoder(String string) throws IOException {
        byte[] decodedArrayByte = Base64.decodeBase64(string);
        return new String(decodedArrayByte);

    }

    @Test
    void pushtogithub() throws IOException {
        GithubService githubService = new GithubService();
        RepoResponsePush repoResponsePush = githubService
            .pushToGithub(".travis.yml","zanio/semicolon-devop-backend",".travis.yml",authId);

        log.info("The file is {}",repoResponsePush);
    }

}