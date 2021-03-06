package com.semicolondevop.suite.client.repository;

import com.semicolondevop.suite.client.authenticationcontext.IAuthenticationFacade;
import com.semicolondevop.suite.client.genericresponse.ResponseApi;
import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.model.repository.dao.get.GithubRepoResponseDao;
import com.semicolondevop.suite.repository.app.AppRepository;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.service.repository.RepositoryService;
import com.semicolondevop.suite.util.github.GithubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private  IAuthenticationFacade iAuthenticationFacadeImpl;

    @Autowired
    private DeveloperRepository developerRepositoryImpl;

    @Autowired
    private RepositoryService repositoryServiceImpl;

    @Autowired
    private AppRepository appRepositoryImpl;

    @GetMapping("/")
    @ApiOperation(value = "Search for Repository which user has write and read access",
            notes = "It search on github for repository which user belongs too")
    public ResponseEntity<?> searchForGitHubRepository(
            @ApiParam(required = true, name = "full_name", value = "Repository full name ('<username>/<repo_name>')") @RequestParam("full_name") String full_name,
            @ApiParam(hidden = true) @RequestHeader Map<String, String> headers) {

        GithubRepoResponseDao repository = null;
        Developer developer = securityContextDeveloper();
        String authId = developer.getAuthId();

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

    private String securityContextUser(){
        return iAuthenticationFacadeImpl.getAuthentication().getName();
    }

    private Developer securityContextDeveloper(){
        String user = iAuthenticationFacadeImpl.getAuthentication().getName();
        return developerRepositoryImpl.findByEmail(user);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create A Repository ", notes = "Add Repository to an App")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Repository.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public ResponseEntity<?> addRepository(@RequestBody Repository repository,
                                           @ApiParam(required = true, name = "id",
                                                   value = "Id of app") @RequestParam("id") Integer id,
                                           @ApiParam(hidden = true) HttpServletRequest request) throws Exception {

        App app1 = appRepositoryImpl.findById(id).get();
        Repository response = null;
        if(app1 != null){
             response = repositoryServiceImpl.add(repository,app1);

        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
