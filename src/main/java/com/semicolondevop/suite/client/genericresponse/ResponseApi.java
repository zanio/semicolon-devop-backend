package com.semicolondevop.suite.client.genericresponse;
/*
 *@author tobi
 * created on 29/04/2020
 *
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;


@Getter @Setter
public class ResponseApi {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    private Throwable throwable;

    public ResponseApi(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ResponseApi(HttpStatus status, String message, String error, Throwable throwable) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
        this.throwable = throwable;
    }

    public ResponseApi(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ResponseApi(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
