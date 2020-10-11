package com.semicolondevop.suite.model.applicationUser;


/*
 *@author Aniefiok
 * created on 11/05/2020
 *
 */

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.model.developer.GithubDeveloperDao;
import com.semicolondevop.suite.service.developer.DeveloperService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Table(name="application_user")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser implements GrantedAuthority {

    @Id
    private Integer id;

    @Column(updatable = false, unique = true)
    @NotNull(message = "{NotNull.email}")
    private String username;

    @ToString.Exclude
    private String password;

    private boolean isActive;

    private String role;

    @OneToOne(mappedBy = "applicationUser")
    @JsonManagedReference
    private UserActivityLogs userActivityLogs;



    public ApplicationUser(@NotNull Developer applicationUser) {

        this.id = generateId();
        this.username = applicationUser.getUsername();
        this.password = applicationUser.getPassword();
        this.role = "USER";
        //set isActive to false upon creation
        this.isActive = true;
    }

    public ApplicationUser(@NotNull GithubDeveloperDao githubDeveloperDao) {

        this.id = generateId();
        this.username = githubDeveloperDao.getLogin();
        this.password = githubDeveloperDao.getPassword();
        this.role = "USER";
        //set isActive to false upon creation
        this.isActive = true;
    }

    public ApplicationUser(@NotNull Admin applicationAdmin) {
        this.id = generateId();
        this.username = applicationAdmin.getUsername();
        this.password = applicationAdmin.getPassword();
        this.role = "ADMIN";
        this.isActive = true;
    }

    private Integer generateId(){
        SecureRandom random = new SecureRandom();
        return 4000+ random.nextInt(5999);
    }


    @Override
    public String getAuthority() {
        return role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        String ROLE_PREFIX = "ROLE_";
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new
                org.springframework.security.core.authority
                        .SimpleGrantedAuthority(ROLE_PREFIX + getAuthority()));
        return list;
    }
}
