package com.semicolondevop.suite.service.jenkins;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.semicolondevop.suite.dao.AccessTokenResponse;
import com.semicolondevop.suite.dao.CreateJenkinsCredentials;
import com.semicolondevop.suite.dao.CredentialPayload;
import com.semicolondevop.suite.dao.webhook.WebhookResponse;
import com.semicolondevop.suite.exception.restTemplate.MyRestTemplateException;
import com.semicolondevop.suite.model.crumb.Crumb;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.jenkins.JenkinsCredentials;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.model.repository.dao.post.RepoResponsePush;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.github.GithubRepository;
import com.semicolondevop.suite.repository.jenkinsCredential.JenkinsCredentialRepository;
import com.semicolondevop.suite.service.json.JsonObject;
import com.semicolondevop.suite.util.github.GithubService;
import com.semicolondevop.suite.util.helper.Encoder_Decoder;
import com.semicolondevop.suite.util.helper.ModifyXMLFile;
import com.semicolondevop.suite.util.helper.RandomString;
import com.semicolondevop.suite.util.http.HttpConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 30/10/2020 - 5:59 PM
 * @project com.semicolondevop.suite.service.jenkins in ds-suite
 */

@Service
@Slf4j
public class JenkinsServiceImpl extends BasicConfig {

    @Autowired
    private JsonObject jsonObject;

    @Value("${github.auth.url}")
    private String githubAuthUrl;

    private final GithubService githubService = new GithubService();


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

    @Autowired
    private JenkinsCredentialRepository jenkinsCredentialRepositoryImpl;

    public void setUpAppForJenkinsCI(Repository repository) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("Pizzly-Auth-Id", authId);
        stringMap.put("Content-Type", "application/json");

        HttpConnection<AccessTokenResponse, String>
                httpConnection = new HttpConnection<>(AccessTokenResponse.class,
                githubAuthUrl, stringMap);
        try {

            ResponseEntity<AccessTokenResponse> accessTokenResponse =
                    httpConnection
                            .getService("api/github/authentications/" + authId, null);
            String accessToken = accessTokenResponse.getBody().getPayload().getAccessToken();

            repository.getApp().getDeveloper().setAccessToken(accessToken);
//                Generate a Crumb token
            Developer developer = repository.getApp().getDeveloper();
            String token = "basic " + Encoder_Decoder.encodeStr("semicolon-devops:semicolon-devops");
            Map<String, String> headerConfig = new HashMap<>();
            headerConfig.put("Authorization", token);
            headerConfig.put("Content-Type", "application/json");

            HttpConnection<Crumb, String>
                    httpConnectionCrumb = new HttpConnection<>(Crumb.class,
                    jenkinsUrl, headerConfig);
            ResponseEntity<Crumb> crumbResponseEntity =
                    httpConnectionCrumb.getService("crumbIssuer/api/json", null);
//              CRUMB DATA
            log.info("THE CRUMB DATA {}", crumbResponseEntity.getBody());
            Crumb crumb = crumbResponseEntity.getBody();
            if (crumb != null) {
                crumb.setCrumbSession(crumbResponseEntity.getHeaders().get("Set-Cookie").get(0));
            }

            log.info("The crumb " +
                    "header info {}", crumb.getCrumbSession());
            crumb.setDeveloper(developer);
            log.info("THE CRUMB DATA {}", crumb);

//               Create Jenkins Credential
            Map<String, String> headerConfigJenkinsCredential = new HashMap<>();

            headerConfigJenkinsCredential.put("Accept", "application/json");
            headerConfigJenkinsCredential.put(crumb.getCrumbRequestField(), crumb.getCrumb());
            headerConfigJenkinsCredential.put("Authorization", token);
            headerConfigJenkinsCredential.put("Content-Type", "application/x-www-form-urlencoded");
            headerConfigJenkinsCredential.put("Cookie", crumb.getCrumbSession());

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

            CreateJenkinsCredentials createJenkinsCredentials = new CreateJenkinsCredentials();
            createJenkinsCredentials.setDescription("A new credential generated for user " + developer.getUsername());
            createJenkinsCredentials.setPassword(accessToken);
            createJenkinsCredentials.setUsername(developer.getUsername());
            CredentialPayload credentialPayload = new CredentialPayload();
            credentialPayload.setCreateJenkinsCredentials(createJenkinsCredentials);
            JenkinsCredentials jenkinsCredentials = new JenkinsCredentials(credentialPayload, developer);

            jenkinsCredentialRepositoryImpl.save(jenkinsCredentials);

            ObjectNode objectNode = jsonObject.convObjToONode(credentialPayload);

            map.add("json", objectNode);

            HttpConnection<String, MultiValueMap<String, Object>>
                    httpConnection1 = new HttpConnection<>(String.class,
                    jenkinsUrl, headerConfigJenkinsCredential);

            ResponseEntity<String> successResponse =
                    httpConnection1.postService("credentials/store/system/domain/_/createCredentials", map);
            log.info("The request was successful {}", successResponse);

            ModifyXMLFile modifyXMLFile = new ModifyXMLFile();
            String github = "https://github.com/" + repository.getFullName();


            modifyXMLFile.updateCredentialsAndRepoLink(credentialPayload.getCreateJenkinsCredentials().getId(),
                    "I JUST CREATED A NEW JOB", github);

            String config = payloadFromResource("/jenkins/jobs/jenkins-pipeline.xml");
            log.info("THE CONFIG {}", config);
            String output = client.api().jobsApi().config(null, developer.getUsername());
            if (output == null) {
                String configs = payloadFromResource("/jenkins/folder-config.xml");
                RequestStatus success1 = client.api().jobsApi().create(null, developer.getUsername(), configs);
                if(success1.value()){
                    int size = client.api().jobsApi().jobList(developer.getUsername()).jobs().size() + 1;

                    RequestStatus success = client.api().jobsApi().create(developer.getUsername(),
                            new RandomString(12).nextString(size + "-"
                                    + repository.getApp().getDeveloper().getUsername()
                                    + "-" + repository.getApp().getName()), config);

                    log.info("THE JOB WAS SUCCESSFULLY CREATED {}", success);
                    if (success.value()) {
                        String webhookUrlValue = "/repos/" + repository.getFullName() + "/hooks";
                        WebhookResponse webhookResponse =
                                githubService.createGithubWebHook(authId, webhookUrlValue, jenkinsUrl + "/github-webhook/");
                        log.info("THE WEBHOOK WAS CREATED SUCCESSFULLY {}", webhookResponse);
                        RepoResponsePush repoResponsePush = githubService
                                .pushToGithub("config/backend/Jenkinsfile",
                                        repository.getFullName(),
                                        "Jenkinsfile", authId);
                        log.info("THE REPO RESPONSE IS AS FOLLOW {}", repoResponsePush);

                    }
                }

            }


        } catch (MyRestTemplateException e) {
            log.info("MyResTemplateException Error {}", e.getError());
            throw e;
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }

    }
}
