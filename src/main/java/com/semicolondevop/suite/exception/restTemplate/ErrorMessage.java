package com.semicolondevop.suite.exception.restTemplate;

import lombok.*;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 29/10/2020 - 8:57 AM
 * @project com.semicolondevop.suite.exception.restTemplate in ds-suite
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    String servlet;
    String message;
    String url;
    String status;
}
