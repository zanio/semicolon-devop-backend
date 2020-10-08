package com.semicolondevop.suite.model.developer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 3:15 PM
 * @project com.semicolondevop.suite.model.developer in ds-suite
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PushToGithubDao {
    private  String content;
    private String sha;
    private String branch;
    private String message;

    public PushToGithubDao(String content, String branch, String message) {
        this.content = content;
        this.branch = branch;
        this.message = message;
    }
}
