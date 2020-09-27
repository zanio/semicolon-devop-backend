package com.semicolondevop.suite.service.developer;
/*
 *@author Aniefiok Akpan
 * created on 09/05/2020
 *
 */


import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userdetails")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService {

    @Qualifier("user")
    @Autowired
    private UserRepository applicationUserRepository;

    @Value("${application.role}")
    private String role;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

// This code is run first before the 'security/JWTAuthenticationFilter'. It first of search the database
// if such user exit and saves the details on the User bean
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        log.info("SEARCHING THE DB FOR USER BY USERNAME '{}'", username);
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);


        if(applicationUser == null || !applicationUser.isActive())
            throw new UsernameNotFoundException(String.format("APPLICATION USER'%s' NOT FOUND", username));
        User user = null;

//
        if(applicationUser.getRole().equals(role)){

            user = new User(applicationUser.getUsername(),
                    applicationUser.getPassword(),
                    applicationUser.getAuthorities());

        }
        else if
        (applicationUser.getAuthorities().toString().equals("[ROLE_ADMIN]")) {

            user = new User(applicationUser.getUsername(),
                    applicationUser.getPassword() ,
                    applicationUser.getAuthorities());
        }else{
            user = new User(applicationUser.getUsername(),
                    applicationUser.getPassword(), applicationUser.getAuthorities());
        }

        return user;
    }


}
