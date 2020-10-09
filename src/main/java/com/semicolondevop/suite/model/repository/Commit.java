package com.semicolondevop.suite.model.repository;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:15 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Commit {
    private String sha;
    private String node_id;
    private String url;
    private String html_url;
    private String message;
    private Author author;
    private Committer committer;
}
