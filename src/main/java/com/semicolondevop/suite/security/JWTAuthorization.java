package com.semicolondevop.suite.security;
/*
 *@author Aniefiok Akpan
 * created on 09/05/2020
 *
 */


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.semicolondevop.suite.client.authenticationcontext.IAuthenticationFacade;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.service.developer.DeveloperService;
import com.semicolondevop.suite.service.developer.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.semicolondevop.suite.security.SecurityConstansts.*;

@Slf4j
//@Component
public class JWTAuthorization extends BasicAuthenticationFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    DeveloperService developerServiceImpl;

    @Autowired
    private UserRepository applicationUserRepository;

    @Autowired
    private AuthenticationManagerBuilder auth;

    @Autowired
    private IAuthenticationFacade authenticationFacade;


    public JWTAuthorization(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        log.info("NEW REQUEST {}", request.getHeader(HEADER_STRING));
        String header = request.getHeader(HEADER_STRING);


        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        log.info("The UsernamePasswordAuthenticationToken from JWTAuthorization {}", authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);
        log.info("HEADER GET {} ", token);
        if (token != null) {
            //parse the token
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();

            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ADMIN");

                return new UsernamePasswordAuthenticationToken(user, null,
                        null);
            }
//            return null;
        }
        log.info("TOKEN IS NULL!!!");
        return null;
    }


    @Bean
    public UsernamePasswordAuthenticationToken getUserFromAuthentication(HttpServletRequest request) {
        log.info("this is the jwt bean");
        return this.getAuthentication(request);
    }

}
