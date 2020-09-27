package com.semicolondevop.suite.client.exception;

/* Aniefiok
 *created on 5/18/2020
 *inside the package */

public class SaverNotFoundByEmail extends RuntimeException {

        public SaverNotFoundByEmail(String entity,String message){
            super(String.format("%s with the email of: %s cannot be found", entity, message));
        }

}
