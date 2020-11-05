package com.semicolondevop.suite.dao;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 04/11/2020 - 10:52 AM
 * @project com.semicolondevop.suite.dao in ds-suite
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GithubRepoContentListFiles {
    private String path;
    private String mode;
    private String type;
    private String sha;
    private Integer size;
    private String url;
}
