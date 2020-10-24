package com.semicolondevop.suite.model.activity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semicolondevop.suite.model.developer.Developer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 12/10/2020 - 4:51 AM
 * @project com.semicolondevop.suite.model.activity in ds-suite
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    private String userAgent;
    private String ip;
    private String expires;

    @OneToOne
//    @JsonBackReference
    private Developer developer;

    private String requestMethod;
    private String url;

    private Long totalVisitors;


}


