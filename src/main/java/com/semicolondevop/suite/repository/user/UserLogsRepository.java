package com.semicolondevop.suite.repository.user;
/*
 *@author Aniefiok Akpan
 * created on 14/06/2020
 *
 */

import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.semicolondevop.suite.model.applicationUser.UserActivityLogs;
import com.semicolondevop.suite.model.developer.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLogsRepository extends JpaRepository<UserActivityLogs, Integer> {
    @Query("SELECT s FROM UserActivityLogs s WHERE s.applicationUser = :applicationUser")
    UserActivityLogs findByApplicationUser(@Param("applicationUser") ApplicationUser applicationUser);
}
