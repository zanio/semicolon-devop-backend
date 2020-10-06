package com.semicolondevop.suite.service.admin;

/* Aniefiok Akpan
 *created on 5/16/2020
 *inside the package */

import com.semicolondevop.suite.client.exception.ResourceNotFound;
import com.semicolondevop.suite.client.exception.UserAlreadyExistException;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.repository.admin.AdminRepository;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.user.UserRepositoryAdmin;
import com.semicolondevop.suite.service.developer.DeveloperService;
import com.semicolondevop.suite.service.developer.DeveloperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("adminservice")
public class AdminServiceImpl extends DeveloperServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    @Qualifier("saver")
    private DeveloperService developerServiceImp;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    @Qualifier("adminRepo")
    private UserRepositoryAdmin userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public Admin save(Admin admin) throws UserAlreadyExistException {
        if (emailExists(admin.getUsername())) {

            log.error("USER EMAIL ACCOUNT ALREADY EXISTS <--> THROWING EXCEPTION");
            throw new UserAlreadyExistException("There is an account with that email adress: " + admin.getUsername());
        } else {

            log.info("CREATING NEW USER ACCOUNT!");
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));

            ApplicationUser details = new ApplicationUser(admin);
            userRepository.save(details);

            log.info("ADMIN SAVED {} --> " , details);

            admin.setApplicationUserId(details);
            log.info("ADMIN DETAILS UPDATED --> {} " , admin);

        }
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> findAllAdmin() {
        return adminRepository.findAll();
    }

    @Override
    public Admin findAdminById(Integer adminId) {
        return adminRepository
                .findById(adminId).orElseThrow(() -> new ResourceNotFound(Admin.class.getSimpleName(), adminId));

    }

    @Override
    public void deleteById(Integer adminId) {
        adminRepository.deleteById(adminId);
    }


    @Override
    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }


    private boolean emailExists(final String email) {
        return adminRepository.findByEmail(email) != null;
    }


    @Override
    public Admin findUserByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Developer convertPassword(Integer saverId){
        Developer saver = developerRepository.findById(saverId).get();

        passwordEncoder.encode(saver.getPassword());

        return developerRepository.save(saver);
    }


}
