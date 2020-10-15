package com.semicolondevop.suite.model.applicationUser;
/*
 *@author Aniefiok Akpan
 * created on 14/06/2020
 *
 */

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user_activity_log")
public class UserActivityLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn()
    @JsonBackReference
    private ApplicationUser applicationUser;

    @CreationTimestamp
    private Date creationDate;


    private Date lastLoginDate;
}
