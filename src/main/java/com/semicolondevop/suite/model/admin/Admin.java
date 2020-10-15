package com.semicolondevop.suite.model.admin;
/*
 *@author Aniefiok Akpan
 * created on 05/05/2020
 *
 */


import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Admin {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Integer adminId;


    private String firstname;

    private String lastname;

    private String phoneNumber;

    @CreationTimestamp
    @ApiModelProperty(hidden = true,readOnly = true)
    private Date dateJoined;

    private String username;

    private String password;

    @ApiModelProperty(hidden = true)
    private String imageUrl;

    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    private String token;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "application_user_id")
    @ApiModelProperty(hidden = true)
    private ApplicationUser applicationUserId;

    public Admin(@NotNull Admin admin) {
        this.firstname = admin.getFirstname();
        this.lastname = admin.getLastname();
        this.phoneNumber = admin.getPhoneNumber();
        this.username = admin.getUsername();
        this.applicationUserId = admin.getApplicationUserId();
        this.password = admin.getPassword();

    }
}
