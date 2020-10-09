package com.semicolondevop.suite.model.repository;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 09/10/2020 - 6:08 AM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataUpdateRepoPush {
    private  String content;
    private String branch;
    private String message;
    private String sha;
}

