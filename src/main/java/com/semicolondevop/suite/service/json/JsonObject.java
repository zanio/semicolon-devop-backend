package com.semicolondevop.suite.service.json;

/* Aniefiok
 *created on 6/14/2020
 *inside the package */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;

@Component
public class JsonObject {

    public ObjectNode convObjToONode(Object o) {
        StringWriter stringify = new StringWriter();
        ObjectNode objToONode = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(stringify, o);
            objToONode = (ObjectNode) mapper.readTree(stringify.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objToONode;
    }
}
