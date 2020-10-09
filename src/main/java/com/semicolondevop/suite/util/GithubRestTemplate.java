package com.semicolondevop.suite.util;

import com.semicolondevop.suite.model.developer.PushToGithubDao;
import com.semicolondevop.suite.model.developer.PushToGithubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 5:35 PM
 * @project com.semicolondevop.suite.util in ds-suite
 */

@Slf4j
public class GithubRestTemplate<T, R> {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Class<T> tType;

    HttpHeaders headers = new HttpHeaders();

    /**
     *
     * @param authId Github authorization token
     */
    public GithubRestTemplate(String authId, Class<T> tType) {
        headers.add("Pizzly-Auth-Id", authId);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        this.tType = tType;
        log.info("THE CLASS RESTEMPLATE {}",restTemplate);

    }


    /**
     *
     * @param context The url to github
     * @return ResponseEntity of Type T
     * @throws Exception
     */
    public ResponseEntity<T> getService(@NotNull String context, String query) throws Exception {
        HttpEntity<R> entity = new HttpEntity<>(null, headers);
        ResponseEntity<T> response = null;
        try {
            String queryConcate = "?"+query;
            String buildContext = query != null?context+queryConcate:context;
            log.info("THE TYPE OF CLASS {} and the complete path {}",tType,buildContext);
            log.info("the entity {} {}",entity,getGithubRootUrl());
            response = restTemplate.exchange(getGithubRootUrl() + context + queryConcate,
                    HttpMethod.GET, entity, tType);
            log.info("THE RESPONSE IS AS FOLLOW");

        } catch (Exception e){
            log.error("THE ERROR OCCURRED DURING GET AND THE CAUSE OF THE ERROR IS: {}", e.getCause().getLocalizedMessage());
            throw new Exception(e.getCause());
        }

        return response;
    }


    /**
     *
     * @param context IT should Perform method Post
     * @param r
     * @return
     * @throws Exception
     */
    public ResponseEntity<T> postService(String context,R r) throws Exception {
        HttpEntity<R> entity = new HttpEntity<>(r, headers);
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(getGithubRootUrl() + context,
                    HttpMethod.POST, entity, tType);

        } catch (Exception e){
            log.error("THE ERROR OCCURRED DURING POST AND THE CAUSE OF THE ERROR IS:  {}", e.getCause().getLocalizedMessage());
            throw new Exception(e.getCause());
        }

        return response;
    }

    /**
     *
     * @param context IT should Perform method Post
     * @param r
     * @return
     * @throws Exception
     */
    public ResponseEntity<T> putService(String context,R r) throws Exception {
        HttpEntity<R> entity = new HttpEntity<>(r, headers);
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(getGithubRootUrl() + context,
                    HttpMethod.PUT, entity, tType);
            log.info("Response from putService {}",response.getBody());

        } catch (Exception e){
            log.error("THE ERROR OCCURRED DURING PUT AND THE CAUSE OF THE ERROR IS: {}", e.getCause().getLocalizedMessage());
            throw new Exception(e.getCause());
        }

        return response;
    }

    /**
     *
     * @param context IT should Perform method Post
     * @param r
     * @return
     * @throws Exception
     */
    public ResponseEntity<T> patchService(String context,R r) throws Exception {
        HttpEntity<R> entity = new HttpEntity<>(r, headers);
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(getGithubRootUrl() + context,
                    HttpMethod.PATCH, entity, tType);
            log.info("RESPONSE PATCH  {}",response.getBody());

        } catch (Exception e){
            log.error("THE ERROR OCCURRED DURING PATCH AND THE CAUSE OF THE ERROR IS: {}", e.getCause().getLocalizedMessage());
            throw new Exception(e.getCause());
        }

        return response;
    }



    private String getGithubRootUrl(){
        return "https://semicolon-dev-oauth2.herokuapp.com/proxy/github/";
    }



}
