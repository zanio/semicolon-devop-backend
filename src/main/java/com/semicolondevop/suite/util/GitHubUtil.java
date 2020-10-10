package com.semicolondevop.suite.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 11:28 AM
 * @project com.semicolondevop.suite.util in ds-suite
 */


@Slf4j
public class GitHubUtil {
    public static List<GithubRepoResponseDao> listAllRepository(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GithubRepoResponseDao[] githubRepoResponseDaos = null;
        try{
            githubRepoResponseDaos = mapper.readValue(json, GithubRepoResponseDao[].class);

        } catch (JsonProcessingException jsonProcessingException){
//            throw jsonProcessingException;
            log.info("ERROR PROCESSING OBJECT {}",jsonProcessingException.getMessage());
        }

        return new ArrayList(Arrays.asList(githubRepoResponseDaos));

    }
}
