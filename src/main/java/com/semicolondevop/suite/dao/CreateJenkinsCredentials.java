package com.semicolondevop.suite.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 28/10/2020 - 7:06 PM
 * @project com.semicolondevop.suite.dao in ds-suite
 */

@Setter
@Getter
@AllArgsConstructor
@ToString
public class CreateJenkinsCredentials {
    private String scope;
    private String id;
    private String username;
    private String password;
    private String description;

    @JsonProperty("stapler-class")
    private String staplerclass;

    public CreateJenkinsCredentials() {
        this.id = UUID.randomUUID().toString()+"-"+new Date(System.currentTimeMillis()).toString();
        this.scope = "GLOBAL";
        this.staplerclass = "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl";

    }
}
