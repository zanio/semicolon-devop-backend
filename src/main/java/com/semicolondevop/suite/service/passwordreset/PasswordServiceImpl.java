package com.semicolondevop.suite.service.passwordreset;


import com.semicolondevop.suite.exception.ResourceNotFound;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.passwordreset.PasswordReset;
import com.semicolondevop.suite.repository.admin.AdminRepository;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.passwordreset.PasswordResetRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

/* Aniefiok Akpan
 *created on 5/24/2020
 *inside the package */

@Service
@Slf4j
public class PasswordServiceImpl  implements PasswordService{

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepositoryImpl;

    @Autowired
    private DeveloperRepository developerRepositoryImpl;


    @Override
    public ApplicationUser findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public PasswordReset createPasswordReset(ApplicationUser applicationUser) {
        PasswordReset passwordReset = new PasswordReset(applicationUser);
       return passwordResetRepository.save(passwordReset);
    }

    @Override
    public PasswordReset findPasswordResetToken(String token) {
        return passwordResetRepository.findByToken(token);
    }

    @Override
    public PasswordReset save(PasswordReset passwordReset) {
        return passwordResetRepository.save(passwordReset);
    }


    @Override
    public Admin findAdminByEmail(String email) throws ResourceNotFound {
        Admin admin = adminRepositoryImpl.findByEmail(email);
        if(admin==null) throw new ResourceNotFound("admin",email);
        return admin;
    }

    @Override
    public Developer findSaverByEmail(String email) throws ResourceNotFound {
        return developerRepositoryImpl.findByEmail(email);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordReset passToken = passwordResetRepository.findByToken(token);
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                :!passwordConfirmationLinkAlreadyVisited(passToken)?"This link has already been confirmed"
                : null;
    }

    @Override
    public ApplicationUser getUserByPasswordResetToken(String token) throws ResourceNotFound {
        PasswordReset passwordReset = passwordResetRepository.findByToken(token);
        if(passwordReset == null){
            throw  new ResourceNotFound(ApplicationUser.class.getSimpleName(),token);
        }
      return passwordReset.getApplicationUserId();
    }

    @Override
    public void changeUserPassword(ApplicationUser applicationUser, String newPassword) {
        applicationUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(applicationUser);
        if(applicationUser.getRole().equals("ADMIN")){
            Admin admin = adminRepositoryImpl.findByEmail(applicationUser.getUsername());
            if(admin == null){
                throw new ResourceNotFound(Admin.class.getSimpleName(),applicationUser.getUsername());
            }
            admin.setPassword(bCryptPasswordEncoder.encode(newPassword));
            adminRepositoryImpl.save(admin);
        }
        if(applicationUser.getRole().equals("USER")){
            Developer saver = developerRepositoryImpl.findByEmail(applicationUser.getUsername());
            if(saver == null){
                throw  new ResourceNotFound(Developer.class.getSimpleName(), applicationUser.getUsername());
            }
            saver.setPassword(bCryptPasswordEncoder.encode(newPassword));
            developerRepositoryImpl.save(saver);
        }

    }

    private boolean isTokenFound(PasswordReset passwordReset) {
        return passwordReset != null;
    }

    private boolean passwordConfirmationLinkAlreadyVisited(PasswordReset passwordReset) {
        return !passwordReset.isPasswordReset();
    }


    private boolean isTokenExpired(PasswordReset passwordReset) {
        final long currentTime = new Date(System.currentTimeMillis()).getTime();
       final long futureTime = passwordReset.getExpiryDate();
        return !(currentTime <= futureTime);
    }


}
