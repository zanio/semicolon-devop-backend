package com.semicolondevop.suite.util;

import com.semicolondevop.suite.model.developer.PushToGithubDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 5:35 PM
 * @project com.semicolondevop.suite.util in ds-suite
 */

@Slf4j
public class GithubRestTemplate<T, R> {

    private RestTemplate restTemplate;

    private Class<T> tType;


    @Value("${url.github}")
    private String githubUrl;

    HttpHeaders headers = new HttpHeaders();

    public GithubRestTemplate(String authId) {
        headers.add("Pizzly-Auth-Id", authId);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        this.tType = (Class<T>) getGenericClassType(0);


    }


    /**
     *
     * @param context The url to github
     * @return ResponseEntity of Type T
     * @throws Exception
     */
    public ResponseEntity<T> getService(String context, String query) throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<T> response = null;
        try {
            response = restTemplate.exchange(getGithubRootUrl() + context+"?"+query,
                    HttpMethod.GET, entity, tType);

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

        } catch (Exception e){
            log.error("THE ERROR OCCURRED DURING PUT AND THE CAUSE OF THE ERROR IS: {}", e.getCause().getLocalizedMessage());
            throw new Exception(e.getCause());
        }

        return response;
    }

    /**
     * Returns a {@link Type} object to identify generic types
     * @return type
     */
    private Type getGenericClassType(int index) {
        // To make it use generics without supplying the class type
        Type type = getClass().getGenericSuperclass();

        while (!(type instanceof ParameterizedType)) {
            if (type instanceof ParameterizedType) {
                type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
            } else {
                type = ((Class<?>) type).getGenericSuperclass();
            }
        }

        return ((ParameterizedType) type).getActualTypeArguments()[index];
    }

    private String getGithubRootUrl(){
        return githubUrl;
    }



}
