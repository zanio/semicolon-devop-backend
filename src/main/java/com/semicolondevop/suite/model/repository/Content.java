package com.semicolondevop.suite.model.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:10 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private String htmlUrl;
    private String url;
    private String name;
    private String path;
    private Commit commit;
}
