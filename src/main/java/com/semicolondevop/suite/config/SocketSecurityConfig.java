package com.semicolondevop.suite.config;

import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/* Aniefiok
 *created on 7/6/2020
 *inside the package */

public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpDestMatchers("/secured/**", "/secured/**/**").authenticated()
//                .anyMessage().authenticated();
//    }
}
