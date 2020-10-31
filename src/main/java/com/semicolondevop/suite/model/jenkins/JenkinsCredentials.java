package com.semicolondevop.suite.model.jenkins;

import com.semicolondevop.suite.dao.CredentialPayload;
import com.semicolondevop.suite.model.developer.Developer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 30/10/2020 - 5:43 PM
 * @project com.semicolondevop.suite.model.jenkins in ds-suite
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JenkinsCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private String credentialsId;

    @OneToOne
    private Developer developer;

    public JenkinsCredentials(CredentialPayload credentialPayload, Developer developer) {
        this.credentialsId = credentialPayload.getCreateJenkinsCredentials().getId();
        this.developer = developer;
    }
}
