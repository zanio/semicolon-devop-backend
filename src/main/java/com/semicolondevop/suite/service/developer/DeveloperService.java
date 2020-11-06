package com.semicolondevop.suite.service.developer;


import com.semicolondevop.suite.client.dto.DeveloperDto;
import com.semicolondevop.suite.exception.UserAlreadyExistException;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;

import java.util.List;

public interface DeveloperService {

     Developer registerAccount(GithubDeveloperDao githubDeveloperDao) throws UserAlreadyExistException;

     List<Developer> findAll();

     Developer findById(Integer id);

     void Delete(Integer id);

     void saveUserToken(String token, Developer developer);

     String generateToken();


     Developer getToken(String token);

     Developer updateDetails(DeveloperDto developerDto);

     Developer update(Developer developer);

     Developer findByEmail(String email);

}
