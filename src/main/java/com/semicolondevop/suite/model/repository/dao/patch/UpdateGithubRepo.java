package com.semicolondevop.suite.model.repository.dao.patch;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 09/10/2020 - 1:23 PM
 * @project com.semicolondevop.suite.model.repository.dao.patch in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateGithubRepo {
    private String name;
    private String full_name;
    private String default_branch;

    public UpdateGithubRepo(String default_branch) {
        this.default_branch = default_branch;
    }
}
