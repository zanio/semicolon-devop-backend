package com.semicolondevop.suite.client.event;

/* Aniefiok
 *created on 5/17/2020
 *inside the package */

import com.semicolondevop.suite.model.admin.Admin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnAdminRegisterationEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private Admin user;
    private String rawPassword;

    public OnAdminRegisterationEvent(Admin user,Locale locale, String appUrl,String rawPassword) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.rawPassword=rawPassword;
    }
}
