package com.semicolondevop.suite.client.exception;
/*
 *@author tobi
 * created on 07/05/2020
 *
 */

public class UserAlreadyExistException extends Exception{

    public UserAlreadyExistException(String message){
        super(message);
    };
}
