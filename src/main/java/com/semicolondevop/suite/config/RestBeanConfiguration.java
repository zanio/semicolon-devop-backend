package com.semicolondevop.suite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author with Username zanio and fullname Aniefiok Akpan
 * @created 09/10/2020 - 5:23 AM
 * @project com.semicolondevop.suite.config in ds-suite
 */

@Configuration
public class RestBeanConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
