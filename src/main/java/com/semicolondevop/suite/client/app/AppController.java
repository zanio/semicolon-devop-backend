package com.semicolondevop.suite.client.app;

import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.service.app.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final AppService appServiceImpl;

    @Autowired
    public AppController(AppService appServiceImpl) {
        this.appServiceImpl = appServiceImpl;
    }

    @PostMapping("/")
    @ApiOperation(value = "Create An Application ", notes = "Create your account to start runing test on our platform")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = App.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public ResponseEntity<?> registerSaver(@RequestBody App app,
                                           @ApiParam(hidden = true) HttpServletRequest request) {
        App app1 = new App(app);
        App response = appServiceImpl.add(app1);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
