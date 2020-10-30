package com.semicolondevop.suite.client.developer;


import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.JobList;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.semicolondevop.suite.dao.AccessTokenResponse;
import com.semicolondevop.suite.dao.CreateJenkinsCredentials;
import com.semicolondevop.suite.dao.CredentialPayload;
import com.semicolondevop.suite.exception.restTemplate.MyRestTemplateException;
import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.crumb.Crumb;
import com.semicolondevop.suite.model.developer.*;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.model.repository.dao.post.RepoResponsePush;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.github.GithubRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.service.json.JsonObject;
import com.semicolondevop.suite.util.github.GithubService;
import com.semicolondevop.suite.util.helper.Encoder_Decoder;
import com.semicolondevop.suite.util.helper.ModifyXMLFile;
import com.semicolondevop.suite.util.helper.RandomString;
import com.semicolondevop.suite.util.http.HttpConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
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

    @Test
    public void it_should_log_in_to_jenkins(){
        String encodedStr = Encoder_Decoder.encodeStr(jenkinsCredentials);
        JenkinsClient client = JenkinsClient.builder()
                .endPoint(jenkinsUrl) // Optional. Defaults to http://127.0.0.1:8080
                .credentials(encodedStr) // Optional.
                .build();


        SystemInfo systemInfo = client.api().systemApi().systemInfo();
//        client.api().jobsApi().create()
        log.info("SYSTEM INFO {}", systemInfo);
        assertThat(systemInfo.jenkinsVersion().equals("2.249.2"));
    }


    public String payloadFromResource(String resource) {
        try {
            return new String(toStringAndClose(getClass().getResourceAsStream(resource)).getBytes(Charsets.UTF_8));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Test
    public void testCreateJob() {
        String config = payloadFromResource("/jenkins/jobs/jenkins-pipeline.xml");
        log.info("THE CONFIG {}", config);
        RequestStatus success = client().api().jobsApi()
                .create("test",new RandomString(12).nextString("zanio".toUpperCase()), config);
        log.info("THE JOB WAS SUCCESSFULLY CREATED {}",success);
        assertThat(success.value()).isTrue();
    }

    @Test
    public void testGetJobListFromRoot() {
        JobList output = client().api().jobsApi().jobList("");
        assertThat(output).isNotNull();
        assertThat(output.jobs().isEmpty()).isFalse();
        log.info("IS JOBS EMPTY {}", output.jobs().isEmpty());
        log.info("TOTAL NUMBER OF JOBS {}", output.jobs().size());
//        assertThat(output.jobs().size();
    }

    @Test
    public void testCreateFoldersInJenkins() {
        String config = payloadFromResource("/jenkins/folder-config.xml");
        String output = client().api().jobsApi().config(null,"test");
        if(output == null){
        RequestStatus success1 = client().api().jobsApi().create(null, "test", config);
        assertThat(success1.value()).isTrue();
        }
        log.info("THE FOLDER IS {}",output );
//

    }


//    @Test
//    public void testGetConfig() {
//        String output = client().api().jobsApi().config(null, "DevTest");
//        assertThat(output).isNotNull();
//    }

    @Test
    public void it_should_create_credentials() throws Exception {
        //       Get the access_token

        Map<String, String> stringMap =  new HashMap<>();
        stringMap.put("Pizzly-Auth-Id",authId);
        stringMap.put("Content-Type","application/json");

        HttpConnection<AccessTokenResponse,String>
                httpConnection = new HttpConnection<>(AccessTokenResponse.class,
                githubAuthUrl,stringMap);

       ResponseEntity<AccessTokenResponse> accessTokenResponse =
               httpConnection
                       .getService("api/github/authentications/"+authId,null);
       log.info("The Response {}", accessTokenResponse.getBody());
      String accessToken = accessTokenResponse.getBody().getPayload().getAccessToken();
      assertThat(accessToken).isNotNull();
//       Generate a Crumb token
        Developer developer = developerRepositoryImpl.findById(46).get();
        String token = "basic "+Encoder_Decoder.encodeStr("semicolon-devops:semicolon-devops");
        Map<String, String> headerConfig =  new HashMap<>();
        headerConfig.put("Authorization", token);
        headerConfig.put("Content-Type","application/json");

        HttpConnection<Crumb,String>
                httpConnectionCrumb = new HttpConnection<>(Crumb.class,
                jenkinsUrl,headerConfig);
        ResponseEntity<Crumb> crumbResponseEntity =
                httpConnectionCrumb.getService("crumbIssuer/api/json",null);
//
        log.info("THE CRUMB DATA {}", crumbResponseEntity.getBody());
        Crumb crumb = crumbResponseEntity.getBody();
        crumb.setCrumbSession(crumbResponseEntity.getHeaders().get("Set-Cookie").get(0));

        log.info("The crumb " +
                "header info {}",crumb.getCrumbSession());
        crumb.setDeveloper(developer);
        log.info("THE CRUMB DATA {}", crumb);
        Map<String, String> headerConfigJenkinsCredential =  new HashMap<>();

        headerConfigJenkinsCredential.put("Accept","application/json");
        headerConfigJenkinsCredential.put(crumb.getCrumbRequestField(),crumb.getCrumb());
        headerConfigJenkinsCredential.put("Authorization",token);
        headerConfigJenkinsCredential.put("Content-Type","application/x-www-form-urlencoded");
        headerConfigJenkinsCredential.put("Cookie",crumb.getCrumbSession());

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        CreateJenkinsCredentials createJenkinsCredentials = new CreateJenkinsCredentials();
        createJenkinsCredentials.setDescription("A new credential generated for user "+developer.getUsername());
        createJenkinsCredentials.setPassword(accessToken);
        createJenkinsCredentials.setUsername(developer.getUsername());
        CredentialPayload credentialPayload = new CredentialPayload();
        credentialPayload.setCreateJenkinsCredentials(createJenkinsCredentials);
        ObjectNode objectNode = jsonObject.convObjToONode(credentialPayload);

        map.add("json",objectNode);

        HttpConnection<String,MultiValueMap<String, Object>>
                httpConnection1 = new HttpConnection<>(String.class,
                jenkinsUrl,headerConfigJenkinsCredential);
//
        try {
            ResponseEntity<String> successResponse =
                    httpConnection1.postService("credentials/store/system/domain/_/createCredentials",map);
            log.info("The request was successful {}", successResponse);
            ModifyXMLFile modifyXMLFile = new ModifyXMLFile();
            Repository repository = githubRepositoryImpl.findById(2).orElseThrow(()->new Exception("Id not find"));
            String github = "https://github.com/"+repository.getFullName();


            modifyXMLFile.updateCredentialsAndRepoLink(credentialPayload.getCreateJenkinsCredentials().getId(),
                            "I JUST CREATED A NEW JOB",github);

            String config = payloadFromResource("/jenkins/jobs/jenkins-pipeline.xml");
            log.info("THE CONFIG {}", config);
            int size = client().api().jobsApi().jobList("test").jobs().size()+1;

            RequestStatus success = client().api().jobsApi().create("test",
                    new RandomString(12).nextString(size+"-"
                            +repository.getApp().getDeveloper().getUsername()+"-"+repository.getApp().getName()), config);
            log.info("THE JOB WAS SUCCESSFULLY CREATED {}",success);

        } catch (MyRestTemplateException e){
            log.info("The error object {}", e.getError());
            throw e;
        }

    }

    @Test
    public void it_should_convert_to_json(){
        CreateJenkinsCredentials createJenkinsCredentials = new CreateJenkinsCredentials();
        createJenkinsCredentials.setDescription("This is the description");
        createJenkinsCredentials.setPassword("accessToken");
        createJenkinsCredentials.setUsername("developer.getUsername()");
        CredentialPayload credentialPayload = new CredentialPayload();
        credentialPayload.setCreateJenkinsCredentials(createJenkinsCredentials);
        ObjectNode objectNode = jsonObject.convObjToONode(credentialPayload);
        log.info("the object node {}", objectNode);
    }

    @Test
    public void it_should_create_a_job_on_jenkins(){

    }

    private JenkinsClient client(){
        String encodedStr = Encoder_Decoder.encodeStr(jenkinsCredentials);
        return JenkinsClient.builder()
                .endPoint(jenkinsUrl) // Optional. Defaults to http://127.0.0.1:8080
                .credentials(encodedStr) // Optional.
                .build();
    }


}