package com.semicolondevop.suite.repository.user;
/*
 *@author Aniefiok Akpan
 * created on 14/06/2020
 *
 */

import com.semicolondevop.suite.model.applicationUser.UserActivityLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogsRepository extends JpaRepository<UserActivityLogs, Integer> {


}
