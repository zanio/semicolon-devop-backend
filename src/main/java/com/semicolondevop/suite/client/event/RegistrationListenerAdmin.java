package com.semicolondevop.suite.client.event;
/*
 *@author Aniefiok Akpan
 * created on 07/05/2020
 *
 */


import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.service.developer.DeveloperService;
import com.semicolondevop.suite.service.email.EmailService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("adminEvent")
@Slf4j
@Setter
@Getter
public class RegistrationListenerAdmin implements ApplicationListener<OnAdminRegisterationEvent> {

    @Autowired
    @Qualifier("saver")
    DeveloperService developerServiceImpl;

    @Autowired
    EmailService emailServiceImpl;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messages;

    @Value("${email.sender}")
    private String emailSender;
    private String url;
    private String subject;
    private Developer saver;
    private Admin admin;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(final OnAdminRegisterationEvent event) {

        this.adminCreation(event);
    }

    private void adminCreation( OnAdminRegisterationEvent event) {

         Admin user = event.getUser();
        this.setUrl(event.getAppUrl()+"/api/admins/new");
        this.setSubject("Admin Notification");
        this.setAdmin(user);
         String recipientAddress = this.getAdmin().getEmail();
         String content = "Congratulations! "+this.getAdmin().getFirstname()+"\n\nYou have been added as an admin " +
                ", Please Login with the following credentials: \n\n Username: "+this.getAdmin().getEmail()+" \n Password: "+  event.getRawPassword()+"" +
                "\n\n using the  url below \n\n: "+this.getUrl()+"\n\n Please endeavor to change your password";
        final SimpleMailMessage email =
                constructEmailMessage(content,recipientAddress,event.getLocale());

        try {
            emailServiceImpl.sendWithSendGrid(email);
        }catch (RuntimeException re){
            throw re;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private SimpleMailMessage constructEmailMessage(final String mess, final String recipientAddress, final Locale content) {
        final String subject = this.getSubject();
        final String message = messages.getMessage("message.regSucc", null, mess, content);
        final String confirmationUrl = this.getUrl();

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n"+confirmationUrl);
        email.setFrom(emailSender);

        return email;
    }
//
}
