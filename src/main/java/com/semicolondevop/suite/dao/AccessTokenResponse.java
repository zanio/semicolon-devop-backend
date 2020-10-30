package com.semicolondevop.suite.dao;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 28/10/2020 - 5:24 AM
 * @project com.semicolondevop.suite.dao in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccessTokenResponse {
    private String setup_id;
    private String auth_id;
    private String id;
    private Payload payload;


}

