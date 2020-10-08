package com.semicolondevop.suite.model.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:15 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Commit {
    private String sha;
    private String NodeId;
    private String url;
    private String htmlUrl;
    private String message;
    private Author author;
    private Committer committer;
}
