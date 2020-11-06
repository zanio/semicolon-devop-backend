package com.semicolondevop.suite.model.login;
/* Aniefiok
 *created on 09/29/2020
 *inside the package */

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class LoginDto {
    private String username;
    private String password;
    
}
