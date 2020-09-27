package com.semicolondevop.suite.client.event;
/*
 *@author tobi
 * created on 07/05/2020
 *
 */

import com.semicolondevop.suite.model.developer.Developer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private Developer user;

    public OnRegistrationCompleteEvent(
            Developer user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    // standard getters and setters
}
