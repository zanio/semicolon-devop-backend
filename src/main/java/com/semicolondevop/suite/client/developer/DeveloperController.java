package com.semicolondevop.suite.client.developer;
/*
 *@author Aniefiok Akpan
 * created on 06/05/2020
 *
 */


import com.semicolondevop.suite.client.event.OnRegistrationCompleteEvent;
import com.semicolondevop.suite.client.exception.UserAlreadyExistException;
import com.semicolondevop.suite.service.cloudinary.CloudinaryService;
import com.semicolondevop.suite.service.json.JsonObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.semicolondevop.suite.client.authenticationcontext.IAuthenticationFacade;
import com.semicolondevop.suite.client.dto.DeveloperDto;
import com.semicolondevop.suite.client.exception.ResourceNotFound;
import com.semicolondevop.suite.client.genericresponse.ResponseApi;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.service.developer.DeveloperService;
import com.semicolondevop.suite.service.json.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/savers")
@Slf4j
@Api(value = "/api/savers", tags = "Saver Services")
public class DeveloperController {

    @Autowired
    private JsonObject jsonObject;


    @Autowired
    @Qualifier("saver")
    private DeveloperService developerServiceImpl;

    @Autowired
    private CloudinaryService cloudinaryServiceImp;


    @Autowired
    private DeveloperAssembler assembler;


    @Autowired()
//    @Qualifier("saverEvent")
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private IAuthenticationFacade authenticationFacade;


    private Developer saver;

    @GetMapping("/auth")
    public ResponseEntity<?> getLoggedInSaver(HttpServletRequest request){

        EntityModel<Developer> entityModel = null;

        entityModel =
                assembler.toModel(developerServiceImpl.findByEmail(authenticationFacade.getAuthentication().getName()));

        return ResponseEntity.created(linkTo(methodOn(DeveloperController.class).findAll()).withRel("savers")
                .toUri())
                .body(entityModel);
    }


    @PostMapping("/new")
    @ApiOperation(value = "register new saver account", notes = "You can register an account with us")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Developer.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public ResponseEntity<?> registerSaver(@RequestBody Developer developer,
                                           @ApiParam(hidden = true) HttpServletRequest request,
                                           @ApiParam(hidden = true) @RequestHeader Map<String, String> headers)
            throws UserAlreadyExistException, URISyntaxException {

        headers.forEach((key, value) -> {
            log.info(String.format("Header '%s' = %s", key, value));
        });

        log.info("Registering user account with information {} from the referer origin" +
                        "and all headers: {}", saver,
                request.getHeader("referer"));

        String baseUrl = headers.containsKey("origin") ? String.format("%s", headers.get("origin")) :
                String.format(" %s://%s", request.getScheme(), headers.get("host"));

        log.info("Is Origin positive or not:->{}", headers.get("origin"));

        EntityModel<Developer> entityModel = null;

        try {


            saver = developerServiceImpl.registerAccount(saver);

            log.info("SUCCESSFULLY SAVED USER --> {}", saver);

            entityModel =
                    assembler.toModel(saver);
            log.info("PUBLISHING EMAIL EVENT  FOR SAVER --> {}", saver);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(developer,
                    request.getLocale(), baseUrl));

        } catch (Exception uaeEx) {
            throw uaeEx;
        }
        log.info(entityModel.toString());
        return ResponseEntity.created(linkTo(methodOn(DeveloperController.class).findAll()).withRel("savers")
                .toUri())
                .body(entityModel);
    }

    /*
     *
     */
    @GetMapping("/all")
    @ApiOperation(value = "Find all Savers", notes = "You can retrieve all savers")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = CollectionModel.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public CollectionModel<EntityModel<Developer>> findAll() {

        List<EntityModel<Developer>> savers =
                developerServiceImpl.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

        return new CollectionModel<>(savers,
                linkTo(methodOn(DeveloperController.class).findAll()).withSelfRel());

    }



    @GetMapping("/")
    @ApiOperation(value = "Find a Saver", notes = "You can retrieve a saver")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = EntityModel.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public EntityModel<Developer> findOne(@ApiParam(required = true, name = "id",
            value = "ID of the Saver you want to search for") @RequestParam("id") Integer id) {
        Developer saver = developerServiceImpl.findById(id);
        return assembler.toModel(saver);

    }

    @GetMapping("/dashboard")
    @ApiOperation(value = "Find a Developer by application Security context", notes = "You can retrieve a Developer")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = EntityModel.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public EntityModel<Developer> findOnebyEmail() {
        String email = authenticationFacade.getAuthentication().getPrincipal().toString();
        log.info("The email from findOneByeEmail =>{}", email);
        Developer developer = developerServiceImpl.findByEmail(email);
        if (saver == null) {
            throw new ResourceNotFound(Developer.class.getSimpleName(), email);
        }
        return assembler.toModel(developer);

    }

    @PatchMapping("/upload/dp")
    @ApiOperation(value = "Find a Developer by application Security context and update the user profile picture" +
            "", notes = "You can retrieve a saver")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = EntityModel.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public ResponseEntity<?> uploaderUserProfilePicture(@RequestParam("file") MultipartFile file) {
        String email = authenticationFacade.getAuthentication().getPrincipal().toString();
        Developer developer = developerServiceImpl.findByEmail(email);
        if (saver == null) {
            throw new ResourceNotFound(Developer.class.getSimpleName(), email);
        }
        String url = cloudinaryServiceImp.uploadFile(file);
        log.info("the image url is {}", url);
        developer.setImageUrl(url);
        EntityModel<Developer> entityModel = null;
        try {
            entityModel = assembler.toModel(developerServiceImpl.update(developer));
        } catch (RuntimeException ex) {
            String message = ex.getMessage().getClass().getSimpleName();
            String error = ex.getCause().toString().getClass().getSimpleName();
            log.info("the error {}, the message {}", error, message);
            return new ResponseEntity<>(new ResponseApi(HttpStatus.BAD_REQUEST, message, error), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(entityModel);
    }

    /**
     * @param request
     * @param token
     * @return
     */
    @GetMapping("/confirm")
    @ApiOperation(value = "Confirm account registration", notes = "Confirm link sent to your email")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request",response = ResponseApi.class)
    })
    public ResponseEntity<?> confirmRegistration
    (@ApiParam(hidden = true) WebRequest request,
     @ApiParam(required = true, name = "token", value = "Input the token sent to your email")
     @RequestParam("token") String token) {

        Locale locale = request.getLocale();


        log.info("Retrieving user token");
        //retrieve saver's token
        Developer savedUser = developerServiceImpl.getToken(token);

        if (savedUser == null) {
            return new ResponseEntity(new ResponseApi(HttpStatus.BAD_REQUEST, "token not valid",
                    "user token not found"),HttpStatus.BAD_REQUEST);
        }

        log.info("Setting active status to true");
        //activate user account
        savedUser.getApplicationUser().setActive(true);

        log.info("updating user details in the database");
        //update
        developerServiceImpl.update(savedUser);


        return ResponseEntity.status(HttpStatus.OK)
                .body("user activated successfully");
    }


    @PatchMapping("/update")
    public Developer UpdateDetails(@RequestBody DeveloperDto developerDto){
        log.info("Saver update request --> {}", saver);
        saver = developerServiceImpl.updateDetails(developerDto);
        log.info("After updating saver details --> {}", saver);
        return saver;
    }



}
