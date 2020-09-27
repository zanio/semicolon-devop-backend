package com.semicolondevop.suite.model.passwordreset;

/* Aniefiok
 *created on 5/24/2020
 *inside the package */

import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.UUID;

@Data
@Table(name="password_reset")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PasswordReset {
//    After 1hr it should expire
    private static final long EXPIRATION = 3_600_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "application_user_id")
    private ApplicationUser applicationUserId;

    private long expiryDate;

    private boolean isPasswordReset;

    public PasswordReset(@NotNull ApplicationUser applicationUser) {
        this.token = UUID.randomUUID().toString();
        this.applicationUserId = applicationUser;
        this.expiryDate=this.calculateExpiryDate();
        this.isPasswordReset = false;
    }
    private long calculateExpiryDate(){
        return new Date(System.currentTimeMillis()+EXPIRATION).getTime();
    }
}
