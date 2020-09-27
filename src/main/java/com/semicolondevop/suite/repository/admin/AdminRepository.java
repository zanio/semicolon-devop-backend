package com.semicolondevop.suite.repository.admin;

/* Aniefiok
 *created on 5/16/2020
 *inside the package */

import com.semicolondevop.suite.model.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT s FROM Admin s WHERE s.email = :email")
    Admin findByEmail(@Param("email") String email);



}
