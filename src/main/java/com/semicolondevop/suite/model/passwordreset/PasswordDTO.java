package com.semicolondevop.suite.model.passwordreset;

/* Aniefiok
 *created on 5/24/2020
 *inside the package */

import lombok.Data;

@Data
public class PasswordDTO {

    private String oldPassword;

    private  String token;

    private String newPassword;
}
