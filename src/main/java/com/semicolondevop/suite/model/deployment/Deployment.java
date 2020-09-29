package com.semicolondevop.suite.model.deployment;

/* Aniefiok
 *created on 09/29/2020
 *inside the package */

import com.semicolondevop.suite.model.app.App;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private ProviderType providerType;

    private String secretKey;

    private String pemFile;

    private String policy;

    private String policyName;

    private String iamPassword;

    private String accountAlias;

    @OneToOne
    @JoinColumn
    private App app;
}
