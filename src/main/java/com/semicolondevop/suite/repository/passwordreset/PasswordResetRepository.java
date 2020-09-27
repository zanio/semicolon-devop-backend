package com.semicolondevop.suite.repository.passwordreset;/* Aniefiok
 *created on 5/24/2020
 *inside the package */

import com.semicolondevop.suite.model.passwordreset.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {
    @Query("SELECT s FROM PasswordReset s WHERE s.token = :token")
    PasswordReset findByToken(@Param("token") String token);
}
