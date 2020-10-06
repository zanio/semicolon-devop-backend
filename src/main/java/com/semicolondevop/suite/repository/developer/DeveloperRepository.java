package com.semicolondevop.suite.repository.developer;
/*
 *@author Aniefiok Akpan
 * created on 06/05/2020
 *
 */

import com.semicolondevop.suite.model.developer.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Integer> {

    @Query("SELECT s FROM Developer s WHERE s.username = :email")
    Developer findByEmail(@Param("email") String email);

    @Query("SELECT s FROM Developer s WHERE s.authId = :token")
    Developer findByToken(@Param("token") String token);



}
