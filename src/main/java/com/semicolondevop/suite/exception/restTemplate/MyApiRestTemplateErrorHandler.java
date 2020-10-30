package com.semicolondevop.suite.exception.restTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 29/10/2020 - 8:17 AM
 * @project com.semicolondevop.suite.exception.restTemplate in ds-suite
 */
public class MyApiRestTemplateErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                String httpBodyResponse = reader.lines().collect(Collectors.joining(""));

                // TODO deserialize (could be JSON, XML, whatever...) httpBodyResponse to a POJO that matches the error structure for that specific API, then extract the error message.
                // Here the whole response will be treated as the error message, you probably don't want that.
                String errorMessage = httpBodyResponse;
                ObjectMapper mapper = new ObjectMapper();

                ErrorMessage errorMessage1 = mapper.readValue(errorMessage, ErrorMessage.class);

                throw new MyRestTemplateException(response.getStatusCode(), errorMessage1);
            }
        }
    }


}
