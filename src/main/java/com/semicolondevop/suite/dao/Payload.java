package com.semicolondevop.suite.dao;

import lombok.*;

import java.util.Date;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 28/10/2020 - 12:37 PM
 * @project com.semicolondevop.suite.dao in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payload {
    private String setupId;
    private String accessToken;
    private String refreshToken;
    private Date created_at;

}
