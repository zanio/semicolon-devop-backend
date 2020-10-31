package com.semicolondevop.suite.dao.webhook;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 31/10/2020 - 8:06 AM
 * @project com.semicolondevop.suite.dao in ds-suite
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GithubWebhookConfiguration {
    private String url;
    private String content_type;
    private String insecure_ssl;
}
