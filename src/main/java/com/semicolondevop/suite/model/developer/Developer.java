package com.semicolondevop.suite.model.developer;

/*
 *@author Aniefiok akpan
 * created on 05/05/2020
 *
 */

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolondevop.suite.model.activity.Activity;
import com.semicolondevop.suite.model.applicationUser.ApplicationUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String username;

    private String password;


    @ApiModelProperty(hidden = true)
    @Size(max=160)
    private String imageUrl;

    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Column(unique = true,length = 300)
    private String authId;

    @ApiModelProperty(hidden = true)
    @Column(unique = true,length = 300)
    private String accessToken;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn()
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @ToString.Exclude
    private ApplicationUser applicationUser;

    @OneToOne
    @JsonManagedReference
    @ApiModelProperty(hidden = true)
    private Activity activity;


    public Developer(@NotNull Developer developer) {

        this.firstname = developer.getFirstname();
        this.lastname = developer.getLastname();
        this.phoneNumber = developer.getPhoneNumber();
        this.username = developer.getUsername();
        this.applicationUser = developer.getApplicationUser();
        this.password = developer.getPassword();

    }

    public Developer(@NotNull GithubDeveloperDao githubDeveloperDao){
        this.firstname = githubDeveloperDao.getUserFirstname();
        this.lastname = githubDeveloperDao.getUserLastname();
        this.phoneNumber = githubDeveloperDao.getPhoneNumber();
        this.imageUrl = githubDeveloperDao.getAvatar_url();
        this.password = githubDeveloperDao.getPassword();
        this.authId = githubDeveloperDao.getAuthId();
        this.username = githubDeveloperDao.getLogin();
    }




}
