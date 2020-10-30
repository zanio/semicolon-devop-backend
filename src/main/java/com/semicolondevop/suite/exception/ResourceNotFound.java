package com.semicolondevop.suite.exception;

import lombok.NoArgsConstructor;

/*
 *@author Aniefiok Akpan
 */
@NoArgsConstructor
public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String entity, Integer id) {
//        super("Could not find Investment id:  "+ id);
        super(String.format("%s with the requested id: %s cannot be found", entity, Integer.toString(id)));

    }
    public ResourceNotFound(String entity, String string) {
//        super("Could not find Investment id:  "+ id);
        super(String.format("%s with the requested %s: %s cannot be found", entity, string,string));

    }

    ;

}
