package com.semicolondevop.suite.client.passwordreset;


import com.semicolondevop.suite.client.event.OnPasswordResetEvent;
import com.semicolondevop.suite.client.exception.ResourceNotFound;
import com.semicolondevop.suite.client.genericresponse.ResponseApi;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.passwordreset.PasswordDTO;
import com.semicolondevop.suite.model.passwordreset.PasswordReset;
import com.semicolondevop.suite.service.passwordreset.PasswordServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Aniefiok
 *created on 5/24/2020
 *inside the package */

@Api(value = "/api/password", tags = "Password Services")
@RequestMapping(value = "/api/password")
@RestController
public class PasswordResetController {

    @Autowired
    private PasswordServiceImpl passwordServiceImpl;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostMapping("/reset")
    @ApiOperation(value = "Password Reset Link", notes = "Send password reset link")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request",response = ResponseApi.class)
    })
    public org.springframework.http.ResponseEntity sendPasswordResetLink(
            @ApiParam(required = true, name = "email", value = "email to send password reset link") @RequestParam("email") String userEmail,
                                                   @ApiParam(hidden = true)   HttpServletRequest request) {
        String baseUrl = String.format("%s://%s:%d", request.getScheme(),
                request.getServerName(), request.getServerPort());

        PasswordReset passwordReset = null;

        ApplicationUser applicationUser = passwordServiceImpl.findUserByUsername(userEmail);
        if (applicationUser == null) {
            throw new ResourceNotFound("User", userEmail);
        }
        if (applicationUser.getRole().equals("ADMIN")) {
            Admin admin = passwordServiceImpl.findAdminByEmail(applicationUser.getUsername());
            passwordReset = passwordServiceImpl.createPasswordReset(applicationUser);
            eventPublisher.publishEvent(new
                    OnPasswordResetEvent(baseUrl, request.getLocale(),
                    admin, passwordReset.getToken(), applicationUser.getRole()));
        }
        if (applicationUser.getRole().equals("USER")) {
            Developer saver = passwordServiceImpl.findSaverByEmail(applicationUser.getUsername());
            passwordReset = passwordServiceImpl.createPasswordReset(applicationUser);
            eventPublisher.publishEvent(new
                    OnPasswordResetEvent(baseUrl, request.getLocale(),
                    saver, passwordReset.getToken(), applicationUser.getRole()));
        }
        return new org.springframework.http.ResponseEntity(new ResponseApi(HttpStatus.OK,
                "Check your email for a reset password link"), HttpStatus.OK);
    }

    /**
     * @param request
     * @param httpServletResponse
     * @param token
     * @return null
     */
    @GetMapping("/confirm")
    @ApiOperation(value = "Confirm Reset Link", notes = "Confirm password reset link sent to your email")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request",response = ResponseApi.class)
    })
    public org.springframework.http.ResponseEntity confirmPasswordReset
    (@ApiParam(hidden = true) WebRequest request, @ApiParam(required = true, name = "token", value = "token sent to your mail")
    @RequestParam("token") String token,@ApiParam(hidden = true) HttpServletResponse httpServletResponse)
            throws IOException {
        String validationResponse = passwordServiceImpl.validatePasswordResetToken(token);
        if (validationResponse != null) {
          return new org.springframework.http.ResponseEntity(new ResponseApi(HttpStatus.BAD_REQUEST,
                  validationResponse,"error validating the token"), HttpStatus.BAD_REQUEST);
        }
       return new org.springframework.http.ResponseEntity(new ResponseApi(HttpStatus.OK,
                "Token successfully verify"), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ApiOperation(value = "Update your password", notes = "Update your password")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request",response = ResponseApi.class)
    })
    public org.springframework.http.ResponseEntity savePassword(@RequestBody PasswordDTO passwordDto,
                                                                @ApiParam(hidden = true) HttpServletRequest httpServletRequest) {

        String result = passwordServiceImpl.validatePasswordResetToken(passwordDto.getToken());

        if (result != null) {
            return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST).body("token not valid");
        }

        ApplicationUser applicationUser = passwordServiceImpl.getUserByPasswordResetToken(passwordDto.getToken());
        if (applicationUser != null) {
            PasswordReset passwordReset = passwordServiceImpl.findPasswordResetToken(passwordDto.getToken());
            passwordReset.setPasswordReset(true);
            passwordServiceImpl.save(passwordReset);
            passwordServiceImpl.changeUserPassword(applicationUser, passwordDto.getNewPassword());
            return new org.springframework.http.ResponseEntity(
                    new ResponseApi(HttpStatus.OK,
                            "Password Successfully updated, please proceed to the login page"), HttpStatus.OK);
        } else {
            return new org.springframework.http.ResponseEntity(
                    new ResponseApi(HttpStatus.BAD_REQUEST,
                            "Error processing your request"), HttpStatus.BAD_REQUEST);
        }
    }

}
