package com.semicolondevop.suite.util.http;

import com.semicolondevop.suite.exception.restTemplate.MyApiRestTemplateErrorHandler;
import com.semicolondevop.suite.exception.restTemplate.MyRestTemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 28/10/2020 - 4:33 AM
 * @project com.semicolondevop.suite.util.http in ds-suite
 */

@Slf4j
public final class HttpConnection<T, R> {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Class<T> tType;

    private final HttpHeaders headers = new HttpHeaders();
    
    private final String url;

    /**
     *
     * @param tType Type of class or name of class
     * @param url: The endpoint of the http connection
     */
    public HttpConnection(Class<T> tType, String url, Map<String, String> headerDetails) {
//        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        this.tType = tType;
        addHeader(headerDetails);
        this.url = url;
        log.info("THE CLASS RESTEMPLATE {}",restTemplate);
        restTemplate.setErrorHandler(new MyApiRestTemplateErrorHandler());
//        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().errorHandler(new MyApiRestTemplateErrorHandler());

    }


    /**
     *
     * @param headerDetails: key/value pair header details
     */
    private void addHeader(Map<String, String> headerDetails){
        headerDetails.forEach(headers::add);
    }


    /**
     *
     * @param context The url to github
     * @return ResponseEntity of Type T
     * @throws Exception
     */
    public ResponseEntity<T> getService(@NotNull String context, String query)  throws MyRestTemplateException {
        HttpEntity<R> entity = new HttpEntity<>(null, headers);
        ResponseEntity<T> response = null;

        String queryConcate = "?"+query;
        String buildContext = query != null?context+queryConcate:context;
        log.info("THE TYPE OF CLASS {} and the complete path {}",tType,buildContext);
        log.info("the entity {} {}",entity,getConnectionUrl()+buildContext);
        response = restTemplate.exchange(getConnectionUrl() + buildContext,
                HttpMethod.GET, entity, tType);


        return response;
    }


    /**
     *
     * @param context IT should Perform method Post
     * @param r
     * @return
     * @throws Exception
     */
    public ResponseEntity<T> postService(String context,R r) throws MyRestTemplateException {
        HttpEntity<R> entity = new HttpEntity<>(r, headers);
        ResponseEntity<T> response = null;
        response = restTemplate.exchange(getConnectionUrl() + context,
                HttpMethod.POST, entity, tType);

        return response;
    }

    /**
     *
     * @param context IT should Perform method Post
     * @param r
     * @return
     * @throws Exception
     */
    public ResponseEntity<T> putService(String context,R r)  throws MyRestTemplateException {
        HttpEntity<R> entity = new HttpEntity<>(r, headers);
        ResponseEntity<T> response = null;

        response = restTemplate.exchange(getConnectionUrl() + context,
                HttpMethod.PUT, entity, tType);
        log.info("Response from putService {}",response.getBody());

        return response;
    }

    /**
     *
     * @param context IT should Perform method Post
     * @param r
     * @return
     * @throws Exception
     */
    public ResponseEntity<T> patchService(String context,R r)  throws MyRestTemplateException {
        HttpEntity<R> entity = new HttpEntity<>(r, headers);
        ResponseEntity<T> response = null;

        response = restTemplate.exchange(getConnectionUrl() + context,
                HttpMethod.PATCH, entity, tType);
        log.info("RESPONSE PATCH  {}",response.getBody());

        return response;
    }



    private String getConnectionUrl(){
        return url;
    }



}
