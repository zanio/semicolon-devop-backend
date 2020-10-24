package com.semicolondevop.suite.client.repository;

import com.semicolondevop.suite.client.genericresponse.ResponseApi;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.util.github.GithubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 6:38 PM
 * @project com.semicolondevop.suite.client.repository in ds-suite
 */
@RestController
@RequestMapping("/api/repository")
@Slf4j
@Api(value = "/api/repository", tags = "Repository Services")
public class GithubController {
    private final GithubService githubService = new GithubService();

    @GetMapping("/")
    @ApiOperation(value = "Search for Repository which user has write and read access",
            notes = "It search on github for repository which user belongs too")
    public ResponseEntity<?> searchForGitHubRepository(
            @ApiParam(required = true, name = "full_name", value = "Repository full name ('<username>/<repo_name>')") @RequestParam("full_name") String full_name,
            @ApiParam(hidden = true) @RequestHeader Map<String, String> headers) {

        GithubRepoResponseDao repository = null;
        String authId = headers.get("Pizzly-Auth-Id");

        try {
            repository = githubService.getGitUserRepository(authId,"repos/"+full_name);
        } catch (Exception e) {
            String message = e.getMessage().getClass().getSimpleName();
            String error = e.getCause().toString().getClass().getSimpleName();
            log.info("the error {}, the message {}", error, message);
            return new ResponseEntity<>(new ResponseApi(HttpStatus.BAD_REQUEST, message, error), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository);
    }
}
