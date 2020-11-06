package com.semicolondevop.suite.exception.securitylayerexeception;

/* Aniefiok
 *created on 5/17/2020
 *inside the package */

import com.semicolondevop.suite.service.json.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class CustomFilterResponse {

    private String message;


    private final JsonObject jsonObjectImpl = new JsonObject();


    public CustomFilterResponse( String message,
                                HttpServletResponse httpServletResponse) throws IOException, ServletException {
        errorProperty(message, httpServletResponse);
    }

    private void errorProperty(String message, HttpServletResponse httpServletResponse) throws IOException {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setHeader("Content-type: application/json","Accept: application/json");

        log.info("AuthenticationEntryPoint => {}",httpServletResponse.getHeaderNames());
        this.message = message;


        Map<String,Object> response = new HashMap<>();
        response.put("status",httpServletResponse.getStatus());
        response.put("message",this.message);
        ObjectMapper mapper = new ObjectMapper();
        log.info("The error object {} unauthorized and the status is {}",response.values(), httpServletResponse.getStatus());
        OutputStream out = httpServletResponse.getOutputStream();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, jsonObjectImpl.convObjToONode(response));
        out.flush();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
