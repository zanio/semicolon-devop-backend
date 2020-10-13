package com.semicolondevop.suite.service.repository;

import com.semicolondevop.suite.model.app.App;
import com.semicolondevop.suite.model.repository.Repository;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 10/10/2020 - 7:32 AM
 * @project com.semicolondevop.suite.service.repository in ds-suite
 */

public interface RepositoryService {
    Repository add(Repository repository, App app) throws Exception;
}
