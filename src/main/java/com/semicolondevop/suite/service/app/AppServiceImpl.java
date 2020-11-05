package com.semicolondevop.suite.service.app;

import com.semicolondevop.suite.client.authenticationcontext.IAuthenticationFacade;
import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.techStack.TechStack;
import com.semicolondevop.suite.repository.app.AppRepository;
import com.semicolondevop.suite.repository.developer.DeveloperRepository;
import com.semicolondevop.suite.repository.teckStack.TechStackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 13/10/2020 - 8:08 AM
 * @project com.semicolondevop.suite.service.app in ds-suite
 */

@Service
@Slf4j
public class AppServiceImpl implements AppService {


    private final AppRepository appRepositoryImpl;

    private final IAuthenticationFacade iAuthenticationFacadeImpl;

    private final DeveloperRepository developerRepositoryImpl;

    private final TechStackRepository techStackRepositoryImpl;


    @Autowired
    public AppServiceImpl(IAuthenticationFacade iAuthenticationFacadeImpl, DeveloperRepository developerRepositoryImpl,
                          AppRepository appRepositoryImpl, TechStackRepository techStackRepositoryImpl) {
        this.appRepositoryImpl = appRepositoryImpl;
        this.iAuthenticationFacadeImpl = iAuthenticationFacadeImpl;
        this.developerRepositoryImpl = developerRepositoryImpl;
        this.techStackRepositoryImpl = techStackRepositoryImpl;
    }

    /**
     *
     * @param app
     * @param lang
     * @return app
     */
    @Override
    public App add(App app, String lang) {
        String username = securityContextUser();
        Developer developer = developerRepositoryImpl.findByEmail(username);
        App response = null;
        if(developer == null){
            throw new UsernameNotFoundException("THE USER WAS NOT FOUND");
        }
        App appNew = new App(app);
        TechStack techStack = new TechStack(lang);

        appNew.setDeveloper(developer);
        log.info("THE APP DEVELOPER {}",appNew);
        try{
            log.info("THE TECKSTACK {}",techStack);

            TechStack techStack1 = techStackRepositoryImpl.save(techStack);

            appNew.setTechStack(techStack1);
            response =  appRepositoryImpl.save(appNew);
        }
        catch (Exception e){
            throw e;
        }
        return response;
    }

    private String securityContextUser(){
        return iAuthenticationFacadeImpl.getAuthentication().getName();
    }
}
