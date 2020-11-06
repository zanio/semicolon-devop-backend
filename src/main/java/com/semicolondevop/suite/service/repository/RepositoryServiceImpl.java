package com.semicolondevop.suite.service.repository;

import com.semicolondevop.suite.client.authenticationcontext.IAuthenticationFacade;
import com.semicolondevop.suite.exception.ResourceNotFound;
import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.repository.Repository;
import com.semicolondevop.suite.repository.app.AppRepository;
import com.semicolondevop.suite.repository.github.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 6:30 PM
 * @project com.semicolondevop.suite.service.repository in ds-suite
 */

@Service
public class RepositoryServiceImpl implements RepositoryService {

    private final GithubRepository githubRepositoryImpl;

    private final AppRepository appRepositoryImpl;

    private final IAuthenticationFacade iAuthenticationFacadeImpl;


    @Autowired
    public RepositoryServiceImpl(GithubRepository githubRepositoryImpl, IAuthenticationFacade iAuthenticationFacadeImpl,
                                 AppRepository appRepositoryImpl) {
        this.githubRepositoryImpl = githubRepositoryImpl;
        this.appRepositoryImpl = appRepositoryImpl;
        this.iAuthenticationFacadeImpl = iAuthenticationFacadeImpl;
    }

    @Override
    public Repository add(Repository repository, App app) throws Exception {
        App app1 = appRepositoryImpl
                .findById(app.getId())
                .orElseThrow(() ->
                        new ResourceNotFound(App.class.getSimpleName(), "We are sorry, that App does not exist"));

        Repository repository1 = new Repository(repository, app1);
        try {
            repository1 = githubRepositoryImpl.save(repository1);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return repository1;
    }
}
