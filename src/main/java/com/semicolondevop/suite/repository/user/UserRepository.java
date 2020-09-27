package com.semicolondevop.suite.repository.user;
/*
 *@author Aniefiok Akpan
 * created on 11/05/2020
 *
 */

import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("user")
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    @Query("SELECT s FROM ApplicationUser s WHERE s.username = :email")
    ApplicationUser findByUsername(@Param("email") String email);
}
