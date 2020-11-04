package com.semicolondevop.suite.dao.webhook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 31/10/2020 - 8:22 AM
 * @project com.semicolondevop.suite.dao.webhook in ds-suite
 */
@Getter
@Setter
@ToString
public class WebhookResponse {
    private Integer id;
    private String type;
    private GithubWebhookPayload config;
}
