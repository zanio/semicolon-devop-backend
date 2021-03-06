package com.semicolondevop.suite.security;
/*
 *@author Aniefiok Akpan
 * created on 09/05/2020
 *
 */


import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.applicationUser.UserActivityLogs;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.user.UserLogsRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.service.json.JsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.semicolondevop.suite.security.SecurityConstansts.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private final JsonObject jsonObjectImpl = new JsonObject();

    private DeveloperRepository developerRepositoryImpl;

    private UserLogsRepository userLogsRepositoryImpl;


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext applicationContext) {
        this.authenticationManager = authenticationManager;
        this.developerRepositoryImpl = applicationContext.getBean(DeveloperRepository.class);
        this.userLogsRepositoryImpl = applicationContext.getBean(UserLogsRepository.class);
    }

    // Authenticate the user when he login against the value stored in the database
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            ApplicationUser creds = new ObjectMapper()
                    .readValue(request.getInputStream(), ApplicationUser.class);

            log.info("USER CREDENTIALS ---> " + creds.getUsername() + creds.getPassword());

//            This line of code authenticate the user input value with the one saved in the
//            User bean (The loadByUsername(email) that was called first), if it is true, it
//            returns a new user saved in the UsernamePasswordAuthenticationToken object
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException io) {
            throw io;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        log.info("AUTH IS DONE, CREATING TOKEN");
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));


        log.info("user authorities ---> {}", authResult.getAuthorities());
        log.info("user email ---> {}", ((User) authResult.getPrincipal()).getUsername());
        log.info("user details ---> {}", authResult.getDetails());

        response.setHeader("Content-type: application/json", "Accept: application/json");
        String user = ((User) authResult.getPrincipal()).getUsername();
        Developer developer = developerRepositoryImpl.findByEmail(user);
        log.info("THE APPLICATION USER {}", developer.getApplicationUser());
        ApplicationUser applicationUser = developer.getApplicationUser();
        UserActivityLogs userLogsRepository = userLogsRepositoryImpl.findByApplicationUser(applicationUser);

        Map<String, Object> responseObject = new HashMap<>();
        if (developer != null) {
            responseObject.put("authId", developer.getAuthId());
        }
        if (userLogsRepository != null) {
            responseObject.put("UserActivityLogs", userLogsRepository);
        }
        responseObject.put("status", response.getStatus());
        responseObject.put("role", authResult.getAuthorities());
        responseObject.put("email", ((User) authResult.getPrincipal()).getUsername());
        responseObject.put(HEADER_STRING, TOKEN_PREFIX + token);

        ObjectMapper mapper = new ObjectMapper();
        OutputStream out = response.getOutputStream();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, jsonObjectImpl.convObjToONode(responseObject));
        out.flush();
        out.close();

    }
}
