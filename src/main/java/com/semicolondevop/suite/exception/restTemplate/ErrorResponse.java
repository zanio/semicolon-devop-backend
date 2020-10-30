package com.semicolondevop.suite.exception.restTemplate;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 29/10/2020 - 9:26 AM
 * @project com.semicolondevop.suite.exception.restTemplate in ds-suite
 */

@Setter
@Getter
public class ErrorResponse {

    private String timestamp;

    /** HTTP Status Code */
    private int status;

    /** HTTP Reason phrase */
    private String error;

    /** A message that describe the error thrown when calling the downstream API */
    private String message;

    /** Downstream API name that has been called by this application */
//    private DownstreamApi api;

    /** URI that has been called */
    private String path;

    public ErrorResponse(MyRestTemplateException ex, String path) {
        this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        this.status = ex.getStatusCode().value();
        this.error = ex.getStatusCode().getReasonPhrase();
        this.message = ex.getError().getMessage();
//        this.api = ex.getApi();
        this.path = path;
    }

    // TODO getters ...
    // TODO toString ...
}
