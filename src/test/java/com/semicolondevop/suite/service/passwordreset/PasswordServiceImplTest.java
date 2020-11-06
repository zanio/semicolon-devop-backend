package com.semicolondevop.suite.service.passwordreset;

import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.passwordreset.PasswordReset;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 25/10/2020 - 8:49 PM
 * @project com.semicolondevop.suite.service.passwordreset in ds-suite
 */
@Slf4j
public class PasswordServiceImplTest {
    @Mock
    private PasswordService passwordService;

    @Before
    public void setUp() {
        passwordService = mock(PasswordService.class);
    }

    @Test
    public void whenFindUserByUsernameIsCalled_thenReturnApplicationUser(){
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername("ani");

        when(passwordService.findUserByUsername("ani")).thenReturn(applicationUser);
        ApplicationUser applicationUser1 = passwordService.findUserByUsername("ani");

        verify(passwordService, times(1)).findUserByUsername("ani");

        log.info("The application user is => {}", applicationUser1);

        assertThat(applicationUser1).isNotNull();

    }

    @Test
    public void whenCreatePasswordResetIsCalled_thenReturnPasswordReset(){
        Admin admin = new Admin();
        admin.setPassword("Password1$");
        admin.setAdminId(5);
        admin.setUsername("admin@alaajo.com");
        admin.setLastname("admin");
        admin.setFirstname("Alaajo");
        ApplicationUser applicationUser = new ApplicationUser(admin);
        applicationUser.setId(4);

        PasswordReset passwordReset = new PasswordReset(applicationUser);
        when(passwordService.createPasswordReset(applicationUser)).thenReturn(passwordReset);

        PasswordReset passwordReset1 = passwordService.createPasswordReset(applicationUser);

        verify(passwordService, times(1)).createPasswordReset(applicationUser);

        log.info("The application user is => {}", passwordReset1);

        assertThat(passwordReset1).isNotNull();

    }

    @Test
    public void whenFindPasswordResetTokenIsCalled_thenReturnPasswordReset(){
        Admin admin = new Admin();
        admin.setPassword("Password1$");
        admin.setAdminId(5);
        admin.setUsername("admin@alaajo.com");
        admin.setLastname("admin");
        admin.setFirstname("Alaajo");
        ApplicationUser applicationUser = new ApplicationUser(admin);
        applicationUser.setId(4);

        PasswordReset passwordReset = new PasswordReset(applicationUser);
        when(passwordService.findPasswordResetToken(passwordReset.getToken())).thenReturn(passwordReset);

        PasswordReset passwordReset1 = passwordService.findPasswordResetToken(passwordReset.getToken());

        log.info("Password token generated is =>{}",passwordReset1);

        assertThat(passwordReset1).isNotNull();

    }

}
