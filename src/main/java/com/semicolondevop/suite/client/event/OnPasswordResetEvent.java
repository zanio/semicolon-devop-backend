package com.semicolondevop.suite.client.event;

/* Aniefiok
 *created on 5/24/2020
 *inside the package */

import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@Getter
@Setter
public class OnPasswordResetEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private Developer user;
    private Admin admin;
    private String token;
    private String applicationUser;
    public OnPasswordResetEvent(@NotNull String appUrl,
                            @NotNull Locale locale, @NotNull Developer user,
                                @NotNull String token,@NotNull String applicationUser) {
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user;
        this.token = token;
        this.applicationUser=applicationUser;
    }

    public OnPasswordResetEvent(@NotNull String appUrl, @NotNull Locale locale,
                                @NotNull Admin admin, @NotNull String token,@NotNull String applicationUser) {
        super(admin);
        this.appUrl = appUrl;
        this.locale = locale;
        this.admin = admin;
        this.token = token;
        this.applicationUser=applicationUser;
    }
}
