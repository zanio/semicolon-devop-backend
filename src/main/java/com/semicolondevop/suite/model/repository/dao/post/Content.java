package com.semicolondevop.suite.model.repository.dao.post;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 08/10/2020 - 7:10 PM
 * @project com.semicolondevop.suite.model.repository in ds-suite
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Content {
    private String html_url;
    private String url;
    private String name;
    private String path;
}
