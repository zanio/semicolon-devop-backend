package com.semicolondevop.suite.model.repository.dao.get;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 09/10/2020 - 12:13 PM
 * @project com.semicolondevop.suite.model.repository.dao.get in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GithubRepoResponseDao {
    private String name;
    private String full_name;
    private Boolean privat;
    private String html_url;
    private String description;
    private String url;
    private String deployments_url;
    private String language;
    private String events_url;
    private String default_branch;
    private Owner owner;

}
