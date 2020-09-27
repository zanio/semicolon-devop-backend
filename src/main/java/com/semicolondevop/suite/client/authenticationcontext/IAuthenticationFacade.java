package com.semicolondevop.suite.client.authenticationcontext;

/* Aniefiok
 *created on 5/16/2020
 *inside the package
 *
 * */

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

}
