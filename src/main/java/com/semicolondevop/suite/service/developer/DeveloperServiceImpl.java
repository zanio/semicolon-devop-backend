package com.semicolondevop.suite.service.developer;
/*
 *@author Aniefiok Akpan
 * created on 06/05/2020
 *
 */

import com.semicolondevop.suite.client.authenticationcontext.AuthenticationFacade;
import com.semicolondevop.suite.client.dto.DeveloperDto;

import com.semicolondevop.suite.client.exception.ResourceNotFound;
import com.semicolondevop.suite.client.exception.UserAlreadyExistException;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.repository.user.UserRepository;
import com.semicolondevop.suite.util.JsonNullableUtils;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Double.sum;

@Service("saver")
@Slf4j
public class DeveloperServiceImpl implements DeveloperService {

    @Autowired
    private DeveloperRepository developerRepositoryImp;

    @Autowired
    @Qualifier("user")
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    AuthenticationFacade authenticationFacade;


    private SecureRandom random = new SecureRandom();

    /**
     *
     * @param githubDeveloperDao
     * @return
     * @throws UserAlreadyExistException
     */
    @Override
    public Developer registerAccount(GithubDeveloperDao githubDeveloperDao) throws UserAlreadyExistException {
        Developer developer = null;

        if (emailExists(githubDeveloperDao.getLogin())) {

            log.error("USER EMAIL ACCOUNT ALREADY EXISTS <--> THROWING EXCEPTION");
            throw new UserAlreadyExistException("There is an account with that email address: " + githubDeveloperDao.getLogin());
        }
        else {

            log.info("CREATING NEW USER ACCOUNT!");
            githubDeveloperDao.setPassword(passwordEncoder.encode(githubDeveloperDao.getPassword()));

            ApplicationUser details = new ApplicationUser(githubDeveloperDao);
            userRepository.save(details);

            log.info("USER SAVED {} --> ",details);
         developer = new Developer(githubDeveloperDao);

            developer.setApplicationUser(details);
            log.info("SAVE NEW SAVER DETAILS --> {} ",developer);

        }
        return developerRepositoryImp.save(developer);
    }

    /**
     *
     * @return
     */
    @Override
    public List<Developer> findAll() {
        return developerRepositoryImp.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Developer findById(Integer id) {
        return developerRepositoryImp
        .findById(id).orElseThrow(() -> new ResourceNotFound(Developer.class.getSimpleName(),id));

    }

    /**
     *
     * @param id
     */
    @Override
    public void Delete(Integer id) {

        developerRepositoryImp.deleteById(id);
    }

    /**
     *
     * @param token
     * @param developer
     */
    @Override
    public void saveUserToken(String token, Developer developer) {
        developer.setAuthId(token);
        developerRepositoryImp.save(developer);
    }

    /**
     *
     * @return
     */
    @Override
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        log.info("Generated user token "+ token);
        return token;
    }

    

    /**
     *
     * @param authId
     * @return
     */
    @Override
    public Developer getToken(String authId) {

        return developerRepositoryImp.findByToken(authId);
    }

    /**
     *
     * @param developerDto
     */
    @Override
    public Developer updateDetails(DeveloperDto developerDto) {

        Developer existingSaver = developerRepositoryImp.findByEmail(authenticationFacade.getAuthentication().getName());


        log.info("Fetched user from the DB --> {}", existingSaver);

        JsonNullableUtils.changeIfPresent(developerDto.getLastname(), existingSaver::setLastname);
        JsonNullableUtils.changeIfPresent(developerDto.getPhoneNumber(), existingSaver::setPhoneNumber);
        JsonNullableUtils.changeIfPresent(developerDto.getFirstname(), existingSaver::setFirstname);



        log.info("Before saver details update --> {}", existingSaver);
        return developerRepositoryImp.save(existingSaver);

    }

    /**
     *
     * @param developer
     */
    @Override
    public Developer update(Developer developer) {

       return developerRepositoryImp.save(developer);


    }



  

    private boolean emailExists(final String email) {
        log.info( "emailExist=>{}",developerRepositoryImp.findByEmail(email) != null);
        return developerRepositoryImp.findByEmail(email) != null;
    }

    /**
     * 
     * @param email
     * @return Developer
     */

    @Override
    public Developer findByEmail(String email) {
        return developerRepositoryImp.findByEmail(email);
    }


    
}
