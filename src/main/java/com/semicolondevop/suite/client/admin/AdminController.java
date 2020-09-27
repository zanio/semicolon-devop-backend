package com.semicolondevop.suite.client.admin;

/* Aniefiok
 *created on 5/16/2020
 *inside the package */


import com.semicolondevop.suite.client.authenticationcontext.IAuthenticationFacade;
import com.semicolondevop.suite.client.event.OnAdminRegisterationEvent;
import com.semicolondevop.suite.client.exception.ResourceNotFound;
import com.semicolondevop.suite.client.exception.UserAlreadyExistException;
import com.semicolondevop.suite.client.genericresponse.ResponseApi;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.service.admin.AdminService;
import com.semicolondevop.suite.service.cloudinary.CloudinaryService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/api/admins", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(value = "/api/admins", tags = "Admin Services")
public class AdminController {

    @Autowired
//    @Qualifier("adminservice")
    private AdminService adminServiceImpl;

    @Autowired
    private AdminAssembler adminAssembler;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private IAuthenticationFacade iAuthenticationFacade;

    @Autowired
    private CloudinaryService cloudinaryServiceImp;


    /**
     *
     * @param admin
     * @param request
     * @return
     * @throws UserAlreadyExistException
     * @throws URISyntaxException
     */
    @PostMapping("/new")
    @ApiOperation(value = "Create Admin", notes = "Creating a new Admin", response = Admin.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Admin.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseApi.class)
    })
    public ResponseEntity<?> registerAdmin
            (
                    @RequestBody Admin admin, @ApiParam(hidden = true) HttpServletRequest request) throws UserAlreadyExistException,
            URISyntaxException {

        log.info("Registering admin account with information {}", admin);
        String baseUrl = String.format("%s://%s:%d", request.getScheme(),
                request.getServerName(), request.getServerPort());
        EntityModel<Admin> entityModel = null;
        String rawPassword = admin.getPassword();

        try {

            eventPublisher.publishEvent(new OnAdminRegisterationEvent(admin,
                    request.getLocale(), baseUrl, rawPassword));

            admin = adminServiceImpl.save(admin);

            log.info("SUCCESSFULLY SAVED ADMIN USER --> {}", admin);

            entityModel =
                    adminAssembler.toModel(admin);
            log.info("PUBLISHING EMAIL EVENT  FOR ADMIN --> {}", admin);


        } catch (UserAlreadyExistException uaeEx) {
            log.info("errr:{}", uaeEx);
            throw uaeEx;

        } catch (Exception e) {
            log.info("errr:{}", e);

            throw e;
        }

        log.info(entityModel.toString());

        return ResponseEntity.created(linkTo(methodOn(AdminController.class).findAll()).withRel("admins")
                .toUri())
                .body(entityModel);
    }

    /**
     *
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "Find all Admins", notes =
            "Retrieving the collection of Admins", response = Admin.class, tags = "Admin Services")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = CollectionModel.class)
    })
    public CollectionModel<EntityModel<Admin>> findAll() {

        List<EntityModel<Admin>> admin =
                adminServiceImpl.findAllAdmin().stream()
                        .map(adminAssembler::toModel).collect(Collectors.toList());

        return new CollectionModel<>(admin,
                linkTo(methodOn(AdminController.class).findAll()).withSelfRel());

    }


    @GetMapping("/dashboard")
    @ApiOperation(value = "Find a Saver by application Security context", notes = "You can retrieve aN admin")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = EntityModel.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public EntityModel<Admin> findOneByEmail() {
        String email = iAuthenticationFacade.getAuthentication().getPrincipal().toString();
        log.info("The email from findOneByeEmail =>{}", email);
        Admin admin = adminServiceImpl.findUserByEmail(email);
        if (admin == null) {
            throw new ResourceNotFound(Developer.class.getSimpleName(), email);
        }
        return adminAssembler.toModel(admin);

    }

    @PatchMapping("/upload/dp")
    @ApiOperation(value = "Find an admin by application Security context and update the user profile picture" +
            "", notes = "You can retrieve a saver")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = EntityModel.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ResponseEntity.class)
    })
    public ResponseEntity<?> uploaderUserProfilePicture(@RequestParam("file") MultipartFile file) {
        String email = iAuthenticationFacade.getAuthentication().getPrincipal().toString();
        Admin admin = adminServiceImpl.findUserByEmail(email);
        if (admin == null) {
            throw new ResourceNotFound(Developer.class.getSimpleName(), email);
        }
        String url = cloudinaryServiceImp.uploadFile(file);
        log.info("the image url is {}", url);
        admin.setImageUrl(url);
        EntityModel<Admin> entityModel = null;
        try {
            entityModel = adminAssembler.toModel(adminServiceImpl.update(admin));
        } catch (RuntimeException ex) {
            String message = ex.getMessage().getClass().getSimpleName();
            String error = ex.getCause().toString().getClass().getSimpleName();
            log.info("the error {}, the message {}", error, message);
            return
                    new ResponseEntity<>(new ResponseApi(HttpStatus.BAD_REQUEST, message, error), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(entityModel);

    }

    @PostMapping("/convert")
    public ResponseEntity<Developer> convertSaverPasswordToByScript(@RequestParam Integer userId) {

        Developer saver = adminServiceImpl.convertPassword(userId);

        return new ResponseEntity<>(saver, HttpStatus.OK);
    }

}
