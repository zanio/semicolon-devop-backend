package com.semicolondevop.suite.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 29/10/2020 - 2:53 AM
 * @project com.semicolondevop.suite.dao in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CredentialPayload {
    @JsonProperty("credentials")
    private CreateJenkinsCredentials createJenkinsCredentials;

}
