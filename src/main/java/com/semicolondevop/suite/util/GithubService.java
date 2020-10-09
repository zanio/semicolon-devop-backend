package com.semicolondevop.suite.util;

import com.semicolondevop.suite.model.developer.PushToGithubResponse;
import com.semicolondevop.suite.model.repository.DataUpdateRepoPush;
import com.semicolondevop.suite.model.repository.RepoResponsePush;
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


    public RepoResponsePush pushToGithub(String pathToFileInResourceFolder,String repo,String pathToRemoteFile,
                                          String authId) throws IOException {
        String link = String.format("repos/%s/contents/%s",repo,pathToRemoteFile);
        log.info("The link {}",link);

        ResponseEntity<PushToGithubResponse> response = null;
        ResponseEntity<RepoResponsePush> response1 = null;
        RepoResponsePush repoResponsePush = null;

        GithubRestTemplate<RepoResponsePush, DataUpdateRepoPush> pushGithubRestTemplate = new GithubRestTemplate<>(authId,RepoResponsePush.class);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(pathToFileInResourceFolder).getFile());
        if(file.exists()){
            GithubRestTemplate<PushToGithubResponse, RepoResponsePush>
                    githubResponsePushToGithubDaoGithubRestTemplate =new GithubRestTemplate<>(authId,PushToGithubResponse.class);
            try{

               String query = "ref=master";
                response =  githubResponsePushToGithubDaoGithubRestTemplate.getService(link,query);
                log.info("The response {}",response.getBody());
                String x1d = GithubContentProcess.decoder(response.getBody().getContent());
                String x2d = GithubContentProcess.decoder(GithubContentProcess.encoder(file));
//                If there is changes on github that means we want to push an update to the same repo path
                if(!x1d.equals(x2d)){
                    log.info("PUSHING UPDATE TO GITHUB");
                    DataUpdateRepoPush pushToGithubDao = new DataUpdateRepoPush(GithubContentProcess.encoder(file),
                            "master","update the repo",response.getBody().getSha());
                    response1 = pushGithubRestTemplate.putService(link,pushToGithubDao);
//                    response.ge
                    repoResponsePush = response1.getBody();
                } else {

//                    else we want to push a new file path to the repo
                    log.info("NO UPDATE TO PUSH TO GITHUB");

                }

            } catch (Exception io){
                log.error("THERE WAS AN IO ERROR {}", io);
                throw new IOException(io);

            }

        } else {
            log.error("FILE NOT FOUND IN {}", pathToFileInResourceFolder);
        }

        return  repoResponsePush;
    }
}
