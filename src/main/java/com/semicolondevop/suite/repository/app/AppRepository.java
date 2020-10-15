package com.semicolondevop.suite.repository.app;

import com.semicolondevop.suite.model.app.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 11:42 PM
 * @project com.semicolondevop.suite.repository.app in ds-suite
 */

@Repository
public interface AppRepository extends JpaRepository<App,Integer> {

}
