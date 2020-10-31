package com.semicolondevop.suite.client.developer.jenkins;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.JobList;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.semicolondevop.suite.dao.AccessTokenResponse;
import com.semicolondevop.suite.dao.CreateJenkinsCredentials;
import com.semicolondevop.suite.dao.CredentialPayload;
import com.semicolondevop.suite.dao.webhook.WebhookResponse;
import com.semicolondevop.suite.exception.restTemplate.MyRestTemplateException;
import com.semicolondevop.suite.model.crumb.Crumb;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.github.GithubRepository;
import com.semicolondevop.suite.service.json.JsonObject;
import com.semicolondevop.suite.util.github.GithubService;
import com.semicolondevop.suite.util.helper.Encoder_Decoder;
import com.semicolondevop.suite.util.helper.ModifyXMLFile;
import com.semicolondevop.suite.util.helper.RandomString;
import com.semicolondevop.suite.util.http.HttpConnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 30/10/2020 - 11:43 AM
 * @project com.semicolondevop.suite.client.developer in ds-suite
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = "spring.profiles.active=test")
@Slf4j
@Sql(scripts={"classpath:/db/development/github_repository.sql"})
@ActiveProfiles("test")
@Transactional
public class JenkinsTest extends BasicConfig {

    @Autowired
    private JsonObject jsonObject;

    @Value("${github.auth.url}")
    private String githubAuthUrl;


    @Value("${jenkins.url}")
    private String jenkinsUrl;

    @Value("${auth.id}")
    private String authId;

    @Autowired
    private JenkinsClient client;

    @Autowired
    private GithubRepository githubRepositoryImpl;

    @Autowired
    private DeveloperRepository developerRepositoryImpl;

    @Test
    public void it_should_log_in_to_jenkins(){

        SystemInfo systemInfo = client.api().systemApi().systemInfo();
//        client.api().jobsApi().create()
        log.info("SYSTEM INFO {}", systemInfo);
        assertThat(systemInfo.jenkinsVersion().equals("2.249.2"));
    }



    @Test
    public void testCreateJob() {
        String config = payloadFromResource("/jenkins/jobs/jenkins-pipeline.xml");
        log.info("THE CONFIG {}", config);
        RequestStatus success = client.api().jobsApi()
                .create("test",new RandomString(12).nextString("zanio".toUpperCase()), config);
        log.info("THE JOB WAS SUCCESSFULLY CREATED {}",success);
        assertThat(success.value()).isTrue();
    }

    @Test
    public void testGetJobListFromRoot() {
        JobList output = client.api().jobsApi().jobList("");
        assertThat(output).isNotNull();
        assertThat(output.jobs().isEmpty()).isFalse();
        log.info("IS JOBS EMPTY {}", output.jobs().isEmpty());
        log.info("TOTAL NUMBER OF JOBS {}", output.jobs().size());
//        assertThat(output.jobs().size();
    }

    @Test
    public void testCreateFoldersInJenkins() {
        String config = payloadFromResource("/jenkins/folder-config.xml");
        String output = client.api().jobsApi().config(null,"test");
        if(output == null){
            RequestStatus success1 = client.api().jobsApi().create(null, "test", config);
            assertThat(success1.value()).isTrue();
        }
        log.info("THE FOLDER IS {}",output );
//

    }

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
        String token = "basic "+ Encoder_Decoder.encodeStr("semicolon-devops:semicolon-devops");
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
//       Create Jenkins Credentials
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
//            Create  Job
            ModifyXMLFile modifyXMLFile = new ModifyXMLFile();
            Repository repository = githubRepositoryImpl.findById(1).orElseThrow(()->new Exception("Id not find"));
            String github = "https://github.com/"+repository.getFullName();


            modifyXMLFile.updateCredentialsAndRepoLink(credentialPayload.getCreateJenkinsCredentials().getId(),
                    "I JUST CREATED A NEW JOB",github);

            String config = payloadFromResource("/jenkins/jobs/jenkins-pipeline.xml");
            log.info("THE CONFIG {}", config);
            int size = client.api().jobsApi().jobList("test").jobs().size()+1;

            RequestStatus success = client.api().jobsApi().create("test",
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
    public void it_should_create_a_webhook() throws Exception {
        Repository repository = githubRepositoryImpl.findById(1).orElseThrow(()->new Exception("Id not find"));
        GithubService githubService = new GithubService();
        String webhookUrlValue = "/repos/" + repository.getFullName() + "/hooks";
        List<WebhookResponse> webhookResponseList = githubService.githubWebHooks(authId,webhookUrlValue);
        WebhookResponse webhookResponse = webhookResponseList.stream().findFirst().get();
        if(webhookResponse != null){
            Integer integer = webhookResponseList.stream().findFirst().get().getId();
            githubService.deleteRepositoryWebHookById(authId,webhookUrlValue+"/"+integer);
        }

        WebhookResponse webhookResponseCreateHooks =
                githubService.createGithubWebHook(authId, webhookUrlValue, jenkinsUrl + "github-webhook/");
        log.info("THE WEBHOOK WAS CREATED SUCCESSFULLY {}", webhookResponse);
    }



}
