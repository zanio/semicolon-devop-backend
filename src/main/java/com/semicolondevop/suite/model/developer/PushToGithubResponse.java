package com.semicolondevop.suite.model.developer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 2:43 PM
 * @project com.semicolondevop.suite.model.developer in ds-suite
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PushToGithubResponse {

    private  String content;
    private String sha;

}
