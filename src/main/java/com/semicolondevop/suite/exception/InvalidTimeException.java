package com.semicolondevop.suite.exception;

import lombok.NoArgsConstructor;

/*
 *@author Aniefiok Akpan
 */
@NoArgsConstructor
public class InvalidTimeException extends Exception{

    public InvalidTimeException(String message){
        super(message);
    };
}

