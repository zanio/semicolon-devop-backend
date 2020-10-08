package com.semicolondevop.suite.util;

import com.semicolondevop.suite.model.developer.PushToGithubDao;
import com.semicolondevop.suite.model.developer.PushToGithubResponse;
import com.semicolondevop.suite.model.repository.RepoResponsePush;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:00 PM
 * @project com.semicolondevop.suite.util in ds-suite
 */

@Slf4j
public class GithubService {



    public RepoResponsePush pushToGithub(String pathToFileInResourceFolder,String repo,String pathToRemoteFile,
                                         File pathToLocalFile, String authId) throws IOException {
        String link = String.format("repos/%s/contents/%s",repo,pathToRemoteFile);

        ResponseEntity<PushToGithubResponse> response = null;

        RepoResponsePush repoResponsePush = null;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(pathToFileInResourceFolder).getFile());
        if(file.exists()){

            try{
                String encoder = GithubContentProcess.encoder(pathToLocalFile);
                String decoder = GithubContentProcess.decoder(encoder);

                GithubRestTemplate<PushToGithubResponse, PushToGithubDao>
                       githubResponsePushToGithubDaoGithubRestTemplate =new GithubRestTemplate<>(authId);
               String query = "ref=master";
                response =  githubResponsePushToGithubDaoGithubRestTemplate.getService(link,query);
//                If there is changes on github that means when want to push an update to the same repo path
                if(!Objects.requireNonNull(response.getBody()).getContent().equals(decoder+"\n")){
                    log.info("HELLO");
                } else {
//                    else we want to push a new file path to the repo
                }

            }
            catch (IOException io){
                log.info("THERE WAS AN IO ERROR {}", io.getLocalizedMessage());

            }  catch (Exception e){
                log.info("THERE WAS AN IO ERROR {}", e.getLocalizedMessage());
            }

        } else {
            log.info("FILE NOT FOUND {}", pathToFileInResourceFolder);
        }

        return  repoResponsePush;
    }
}
