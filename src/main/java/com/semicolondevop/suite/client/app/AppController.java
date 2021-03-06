package com.semicolondevop.suite.client.app;

import com.semicolondevop.suite.client.genericresponse.ResponseApi;
import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.model.repository.dao.post.RepoResponsePush;
import com.semicolondevop.suite.repository.github.GithubRepository;
import com.semicolondevop.suite.service.app.AppService;
import com.semicolondevop.suite.service.app.AppServiceImpl;
import com.semicolondevop.suite.service.jenkins.JenkinsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 13/10/2020 - 10:03 AM
 * @project com.semicolondevop.suite.client.app in ds-suite
 */

@RestController
@RequestMapping("/api/app")
@Slf4j
@Api(value = "/api/app", tags = "App Services")
public class AppController {

    @Autowired
    private AppService appServiceImpl ;

    @Autowired
    private GithubRepository githubRepositoryImpl;

    @Autowired
    private JenkinsServiceImpl jenkinsServiceImpl;

//    @Autowired
//    public AppController(AppService appServiceImpl) {
//        this.appServiceImpl = appServiceImpl;
//    }

    @PostMapping("/")
    @ApiOperation(value = "Create An Application ", notes = "Create your account to start runing test on our platform")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = App.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public ResponseEntity<?> registerSaver(@RequestBody App app,
                                           @ApiParam(required = true, name = "lang",
                                           value = "Language or Framework of Application") @RequestParam("lang") String lang,
                                           @ApiParam(hidden = true) HttpServletRequest request) {
        App app1 = new App(app);
        App response = appServiceImpl.add(app1,lang);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/setup")
    @ApiOperation(value = "Set up Jenkins Ci/Cd", notes = "It configure jenkins ci/cd for automation of github repository")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = EntityModel.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public ResponseEntity<?> setUpApp(@ApiParam(required = true, name = "id",
            value = "ID of the repository") @RequestParam("id") Integer id, @ApiParam(hidden = true) WebRequest request) {
        Repository repository = githubRepositoryImpl.findById(id).get();
        if(repository == null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi(HttpStatus.BAD_REQUEST,"The repository was not found"));
        }
        if(!repository.getIsRepoLinkedToJenkins() || repository.getIsRepoLinkedToJenkins() == null){
            RepoResponsePush repoResponsePush =jenkinsServiceImpl.setUpAppForJenkinsCI(repository);
            log.info("After the jenkinsServiceImpl method is called");
            repository.setIsRepoLinkedToJenkins(true);
            Repository repository1 = githubRepositoryImpl.save(repository);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(repository1);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body((new ResponseApi(HttpStatus.BAD_REQUEST,"Application already linked to DS-suite CI/CD")));
        }


    }

}
