package com.semicolondevop.suite.util.github;

import com.semicolondevop.suite.dao.webhook.GithubWebhookConfiguration;
import com.semicolondevop.suite.dao.webhook.GithubWebhookPayload;
import com.semicolondevop.suite.dao.webhook.WebhookResponse;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.model.developer.PushToGithubResponse;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.model.repository.dao.patch.UpdateGithubRepo;
import com.semicolondevop.suite.model.repository.dao.post.DataUpdateRepoPush;
import com.semicolondevop.suite.model.repository.dao.post.RepoResponsePush;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:00 PM
 * @project com.semicolondevop.suite.util in ds-suite
 */

@Slf4j
public final class GithubService {

    /**
     * @param pathToFileInResourceFolder
     * @param repo
     * @param pathToRemoteFile
     * @param authId
     * @return
     * @throws IOException
     */
    public RepoResponsePush pushToGithub(String pathToFileInResourceFolder, String repo, String pathToRemoteFile,
                                         String authId) throws IOException {
        String link = String.format("repos/%s/contents/%s", repo, pathToRemoteFile);
        log.info("THE LINK {}", link);

        ResponseEntity<PushToGithubResponse> response = null;
        ResponseEntity<RepoResponsePush> response1 = null;
        RepoResponsePush repoResponsePush = null;

        GithubRestTemplate<RepoResponsePush, DataUpdateRepoPush> pushGithubRestTemplate = new GithubRestTemplate<>(authId, RepoResponsePush.class);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(pathToFileInResourceFolder).getFile());
        if (file.exists()) {
            GithubRestTemplate<PushToGithubResponse, RepoResponsePush>
                    githubResponsePushToGithubDaoGithubRestTemplate = new GithubRestTemplate<>(authId, PushToGithubResponse.class);
            try {

                String query = "ref=master";
                response = githubResponsePushToGithubDaoGithubRestTemplate.getService(link, query);
                log.info("The response {}", response.getBody());
                String x1d = GithubContentProcess.decoder(response.getBody().getContent());
                String x2d = GithubContentProcess.decoder(GithubContentProcess.encoder(file));
//                If there is changes on github that means we want to push an update to the same repo path
                if (!x1d.equals(x2d)) {
                    log.info("PUSHING UPDATE TO GITHUB");
                    DataUpdateRepoPush pushToGithubDao = new DataUpdateRepoPush(GithubContentProcess.encoder(file),
                            "master", "update the repo", response.getBody().getSha());
                    response1 = pushGithubRestTemplate.putService(link, pushToGithubDao);
//                    response.ge
                    repoResponsePush = response1.getBody();
                } else {

//                    else we want to push a new file path to the repo
                    log.info("NO UPDATE TO PUSH TO GITHUB");

                }

            } catch (Exception io) {
                log.error("THERE WAS AN IO ERROR {}", io);
                throw new IOException(io);

            }

        } else {
            log.error("FILE NOT FOUND IN {}", pathToFileInResourceFolder);
        }

        return repoResponsePush;
    }

    /**
     * @param authId
     * @param context
     * @return
     */

    public GithubDeveloperDao getGitUserProfile(String authId, String context) {
        GithubDeveloperDao githubDeveloperDao = null;
        try {
            GithubRestTemplate<GithubDeveloperDao, GithubDeveloperDao>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, GithubDeveloperDao.class);

            githubDeveloperDao = githubRestTemplate.getService(context, null).getBody();
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserProfile {}", e.getCause().getLocalizedMessage());

        }
        return githubDeveloperDao;
    }

    /**
     * @param authId
     * @param context
     * @return
     */

    public List<GithubRepoResponseDao> getGitUserRepositories(String authId, String context) {
        List<GithubRepoResponseDao> responseDaoList = new ArrayList<>();
        try {
            GithubRestTemplate<GithubRepoResponseDao[], String>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, GithubRepoResponseDao[].class);

            GithubRepoResponseDao[] githubRepoResponseDaos = githubRestTemplate.getService(context, null).getBody();
            if (githubRepoResponseDaos != null && githubRepoResponseDaos.length > 0) {
                responseDaoList = Arrays.asList(Objects.requireNonNull(githubRestTemplate.getService(context, null).getBody()));

            }


        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return responseDaoList;
    }

    /**
     * @param authId
     * @param context
     * @return
     */

    public GithubRepoResponseDao getGitUserRepository(String authId, String context) {
        GithubRepoResponseDao githubRepoResponseDao = null;
        try {
            GithubRestTemplate<GithubRepoResponseDao, GithubRepoResponseDao>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, GithubRepoResponseDao.class);

            githubRepoResponseDao = githubRestTemplate.getService(context, null).getBody();
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return githubRepoResponseDao;
    }

    /**
     * @param authId
     * @param context
     * @param repo_full_name
     * @return
     */

    public List<GithubRepoResponseDao> getGitUserRepositoryAndSortByFullName(String authId, String context, String repo_full_name) {
        List<GithubRepoResponseDao> responseDaoList = new ArrayList<>();
        try {
            GithubRestTemplate<GithubRepoResponseDao[], GithubRepoResponseDao>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, GithubRepoResponseDao[].class);
            String query = "full_name=" + repo_full_name;
            GithubRepoResponseDao[] githubRepoResponseDaos = githubRestTemplate.getService(context, null).getBody();
            if (githubRepoResponseDaos != null && githubRepoResponseDaos.length > 0) {
                responseDaoList = Arrays.asList(Objects.requireNonNull(githubRestTemplate.getService(context, query).getBody()));

            }
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return responseDaoList;
    }

    /**
     * @param authId
     * @param context
     * @return
     */
    public GithubRepoResponseDao updateGithubDefaultBranch(String authId, String context) {
        GithubRepoResponseDao githubRepoResponseDao = null;
        try {
            GithubRestTemplate<GithubRepoResponseDao, UpdateGithubRepo>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, GithubRepoResponseDao.class);
            UpdateGithubRepo updateGithubRepo = new UpdateGithubRepo("master");
            githubRepoResponseDao = githubRestTemplate.patchService(context, updateGithubRepo).getBody();
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return githubRepoResponseDao;
    }


    /**
     * @param authId
     * @param context
     * @param webHookUrl
     * @return WebHookResponse
     */
    public WebhookResponse createGithubWebHook(String authId, String context, String webHookUrl) {
        WebhookResponse webhookResponse = null;
        try {
            GithubRestTemplate<WebhookResponse, GithubWebhookPayload>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, WebhookResponse.class);
            GithubWebhookPayload githubWebhookPayload = new GithubWebhookPayload();
            githubWebhookPayload.setActive(true);
            githubWebhookPayload.setName("web");
            githubWebhookPayload.add("push");
            githubWebhookPayload.add("pull_request");
            GithubWebhookConfiguration githubWebhookConfiguration = new GithubWebhookConfiguration();
            githubWebhookConfiguration.setContent_type("json");
            githubWebhookConfiguration.setInsecure_ssl("1");
            githubWebhookConfiguration.setUrl(webHookUrl);
            githubWebhookPayload.setConfig(githubWebhookConfiguration);
            log.info("THE APPLICATION GITHUBWEBHOOKPAYLOAD {}", githubWebhookPayload);
            webhookResponse = githubRestTemplate.postService(context, githubWebhookPayload).getBody();
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return webhookResponse;
    }

    /**
     *
     * @param authId
     * @param context
     * @return
     */
    public List<WebhookResponse> githubWebHooks(String authId, String context) {
        List<WebhookResponse> responseDaoList = new ArrayList<>();
        try {
            GithubRestTemplate<WebhookResponse[], String>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, WebhookResponse[].class);
            WebhookResponse[] githubRepoResponseDaos = githubRestTemplate.getService(context, null).getBody();
            if (githubRepoResponseDaos != null && githubRepoResponseDaos.length > 0) {
                responseDaoList = Arrays.asList(Objects.requireNonNull(githubRepoResponseDaos));

            }
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return responseDaoList;
    }


    public void deleteRepositoryWebHookById(String authId, String context) {
        try {
            GithubRestTemplate<String, String>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, String.class);

           githubRestTemplate.delete(context, null);
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
    }

}
