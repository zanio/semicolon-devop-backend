package com.semicolondevop.suite.service.credentials;

/* Aniefiok
 *created on 5/12/2020
 *inside the package */

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.semicolondevop.suite.security.SecurityConstansts.*;

@Component
@Slf4j
public class UserCredentials {
    public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);
        log.info("HEADER GET {} ", token);
        if (token != null) {
            //parse the token
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
//            return null;
        }
        log.info("TOKEN IS NULL!!!");
        return null;
    }
}
