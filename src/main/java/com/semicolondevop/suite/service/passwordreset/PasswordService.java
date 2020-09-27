package com.semicolondevop.suite.service.passwordreset;

/* Aniefiok Akpan
 *created on 5/24/2020
 *inside the package */

import com.semicolondevop.suite.client.exception.ResourceNotFound;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.passwordreset.PasswordReset;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface PasswordService {

    ApplicationUser findUserByUsername(String username) throws UsernameNotFoundException;
    PasswordReset createPasswordReset(ApplicationUser applicationUser);
    PasswordReset findPasswordResetToken(String token);
    PasswordReset save(PasswordReset passwordReset);
    Admin findAdminByEmail(String email) throws ResourceNotFound;
    Developer findSaverByEmail(String email) throws ResourceNotFound;
    String validatePasswordResetToken(String token);
    ApplicationUser getUserByPasswordResetToken(String token) throws ResourceNotFound;
    void changeUserPassword(ApplicationUser applicationUser, String newPassword);

}
