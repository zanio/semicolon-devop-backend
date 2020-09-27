package com.semicolondevop.suite.model.developer;
/*
 *@author Aniefiok akpan
 * created on 05/05/2020
 *
 */

import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@Entity()
@Data
@NoArgsConstructor
@DynamicUpdate
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Integer id;
    @NotNull
    private String firstname;
    private String lastname;

    @Column(unique = true)
    @NotNull
    @Size(min=10, max=17)
    private String phoneNumber;

    @CreationTimestamp
    @ApiModelProperty(hidden = true)
    private Date dateJoined;

    @Column(updatable = false, unique = true)
    @NotNull(message = "{NotNull.email}")
    @Size(min=10, max=50)
    private String email;

    private String password;


    @ApiModelProperty(hidden = true)
    @Size(max=160)
    private String imageUrl;

    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    private String token;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn()
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @ToString.Exclude
    private ApplicationUser applicationUser;


    public Developer(@NotNull Developer developer) {

        this.firstname = developer.getFirstname();
        this.lastname = developer.getLastname();
        this.phoneNumber = developer.getPhoneNumber();
        this.email = developer.getEmail();
        this.applicationUser = developer.getApplicationUser();
        this.password = developer.getPassword();

    }




}
