package com.semicolondevop.suite.client.exception;
/*
 *@author tobi
 * created on 06/05/2020
 *
 */

public class SaverNotFoundException extends RuntimeException {

    public SaverNotFoundException(Integer Id){
        super("Could not find saver "+ Id);
    }

    public SaverNotFoundException() {

    }

    public SaverNotFoundException(String s) {
    }
}
