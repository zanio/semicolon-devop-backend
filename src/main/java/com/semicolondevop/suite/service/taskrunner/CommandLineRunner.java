package com.semicolondevop.suite.service.taskrunner;

/* Aniefiok
 *created on 5/19/2020
 *inside the package */

import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    @Autowired
    @Qualifier("user")
    private UserRepository userRepositoryImpl;

    @Autowired
    private DeveloperRepository developerRepositoryImpl;

    @Value("${application.email1}")
    private String email1;

    @Value("${application.email2}")
    private String email2;

    @Value("${application.password1}")
    private String password1;

    @Value("${application.password2}")
    private String password2;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoderImpl;

    @Override
    public void run(String... args) throws Exception {


        List<ApplicationUser> applicationUserList1 = userRepositoryImpl.saveAll(InsertApplicationSuperAdmin());

        ApplicationUser applicationUser1 = userRepositoryImpl.findByUsername(email1);
        ApplicationUser applicationUser2 = userRepositoryImpl.findByUsername(email2);

        List<Developer> developerList = InsertDevelopers(applicationUser1,applicationUser2);

        developerList.forEach((e)->{
           Developer developer = developerRepositoryImpl.findByEmail(e.getUsername());
           if(developer == null){
               developerRepositoryImpl.save(e);
           }
        });



        log.info(
                "Application started with command-line arguments: {} . ", Arrays.toString(args));

        log.info("Application user for SuperAdmin is as follows {}", applicationUserList1);
    }

    private List<ApplicationUser> InsertApplicationSuperAdmin() {
        ApplicationUser applicationUser1 = new ApplicationUser();

        String encodedPassword1 = bCryptPasswordEncoderImpl.encode(password1);
        String encodedPassword2 = bCryptPasswordEncoderImpl.encode(password2);
        applicationUser1.setUsername(email1);
        applicationUser1.setPassword(encodedPassword1);
        applicationUser1.setRole("SUPER_ADMIN");
        applicationUser1.setActive(true);
        applicationUser1.setId(1);


        ApplicationUser applicationUser2 = new ApplicationUser();
        applicationUser2.setUsername(email2);
        applicationUser2.setPassword(encodedPassword2);
        applicationUser2.setRole("SUPER_ADMIN");
        applicationUser2.setActive(true);
        applicationUser2.setId(2);

        List<ApplicationUser> applicationUserList = new ArrayList<>();
        applicationUserList.add(applicationUser1);
        applicationUserList.add(applicationUser2);
        return applicationUserList;
    }

    private List<Developer> InsertDevelopers(ApplicationUser applicationUser, ApplicationUser applicationUser1){
        String encodedPassword1 = bCryptPasswordEncoderImpl.encode(password1);
        String encodedPassword2 = bCryptPasswordEncoderImpl.encode(password2);
        Developer developer = new Developer();
        developer.setUsername(email1);
        developer.setPassword(encodedPassword1);
        developer.setLastname("Aniefiok");
        developer.setFirstname("Akpan");
        developer.setApplicationUser(applicationUser);
        developer.setPhoneNumber("0907145321");
        developer.setId(3);

        Developer developer1 = new Developer();
        developer1.setUsername(email2);
        developer1.setPassword(encodedPassword2);
        developer1.setLastname("Aniefidfok");
        developer1.setFirstname("Akdfpan");
        developer1.setApplicationUser(applicationUser1);
        developer1.setPhoneNumber("09071455321");
        developer1.setId(5);

        List<Developer> developerList = new ArrayList<>();
        developerList.add(developer);
        developerList.add(developer1);

        return developerList;
    }

}
