package com.semicolondevop.suite.util;

import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.model.developer.PushToGithubResponse;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.model.repository.dao.get.ListOfRepository;
import com.semicolondevop.suite.model.repository.dao.patch.UpdateGithubRepo;
import com.semicolondevop.suite.model.repository.dao.post.DataUpdateRepoPush;
import com.semicolondevop.suite.model.repository.dao.post.RepoResponsePush;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:00 PM
 * @project com.semicolondevop.suite.util in ds-suite
 */

@Slf4j
public class GithubService {

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

    public ListOfRepository getGitUserRepositories(String authId, String context) {
        ListOfRepository listOfRepository = null;
        try {
            GithubRestTemplate<ListOfRepository, GithubRepoResponseDao>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, ListOfRepository.class);

            listOfRepository = githubRestTemplate.getService(context, null).getBody();
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return listOfRepository;
    }

    public GithubRepoResponseDao getGitUserRepository(String authId, String context, String query) {
        GithubRepoResponseDao githubRepoResponseDao = null;
        try {
            GithubRestTemplate<GithubRepoResponseDao, GithubRepoResponseDao>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, GithubRepoResponseDao.class);

            githubRepoResponseDao = githubRestTemplate.getService(context, query).getBody();
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return githubRepoResponseDao;
    }

    public GithubRepoResponseDao getGitUserRepositoryByFullName(String authId, String context, String repo_full_name) {
        GithubRepoResponseDao githubRepoResponseDao = null;
        try {
            GithubRestTemplate<GithubRepoResponseDao, GithubRepoResponseDao>
                    githubRestTemplate
                    = new GithubRestTemplate<>(authId, GithubRepoResponseDao.class);
            String query = "full_name=" + repo_full_name;
            githubRepoResponseDao = githubRestTemplate.getService(context, query).getBody();
        } catch (Exception e) {
            log.error("ERROR OCCURRED IN THE getGitUserRepository {}", e.getCause().getLocalizedMessage());
        }
        return githubRepoResponseDao;
    }

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


}
