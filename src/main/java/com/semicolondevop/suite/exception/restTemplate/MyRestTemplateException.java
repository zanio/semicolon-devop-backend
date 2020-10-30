package com.semicolondevop.suite.exception.restTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 29/10/2020 - 8:15 AM
 * @project com.semicolondevop.suite.exception.restTemplate in ds-suite
 */
@Getter
@Setter
@ToString
public class MyRestTemplateException extends RuntimeException{
    private HttpStatus statusCode;
    private ErrorMessage error;

    public MyRestTemplateException( HttpStatus statusCode, ErrorMessage error) {
        super(error.message);
        this.statusCode = statusCode;
        this.error = error;
    }
}
