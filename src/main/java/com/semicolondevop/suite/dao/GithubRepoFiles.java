package com.semicolondevop.suite.dao;

import lombok.*;

import java.util.Set;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 04/11/2020 - 10:50 AM
 * @project com.semicolondevop.suite.dao in ds-suite
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GithubRepoFiles {
    private String sha;
    private String url;
    private Set<GithubRepoContentListFiles> tree;
//    private
}
