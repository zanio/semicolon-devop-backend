package com.semicolondevop.suite.client.event;
/*
 *@author tobi
 * created on 07/05/2020
 *
 */


import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.service.developer.DeveloperService;
import com.semicolondevop.suite.service.email.EmailService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("saverEvent")
@Slf4j
@Setter
@Getter
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

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


    @SneakyThrows
    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {

        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) throws Exception {

        final Developer user = event.getUser();
        final String token = developerServiceImpl.generateToken();
        this.setUrl(event.getAppUrl() + "/api/savers/confirm?token=" + token);
        this.setSubject("Registration Confirmation");
        this.setSaver(user);
        final String recipientAddress = this.getSaver().getEmail();
        final String content = "Congratulations! "+this.getSaver().getFirstname()+"\n\nYou registered successfully. " +
                        "We will send you a confirmation message to your email account.";
        final SimpleMailMessage email = constructEmailMessage(content,recipientAddress,event.getLocale());


        try {
            emailServiceImpl.sendWithSendGrid(email);
            log.info("SAVING TOKEN INFO FOR USER ---> {}", user);
            developerServiceImpl.saveUserToken(token, user);
        }catch (RuntimeException re){
            log.info("unknownException {}=>",re.getMessage());
//            throw re;
        }

    }

    private void adminCreation(final OnAdminRegisterationEvent event) {

        final Admin user = event.getUser();
        this.setUrl(event.getAppUrl()+"/api/admins/new");
        this.setSubject("Registration Confirmation");
        this.setAdmin(admin);
        final String recipientAddress = this.getAdmin().getEmail();
        final String content = "Congratulations! "+this.getAdmin().getFirstname()+"\n\nYou have be added as an admin " +
                ", Please Login to with the following credential: \n\n Email: "+this.getAdmin().getEmail()+" \n Password"+this.getAdmin().getPassword()+"" +
                "\n\n with the following url"+this.getUrl();
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
        final String confirmationUrl = this.getUrl();
        final String message = messages.getMessage("message.regSucc", null, mess, content);

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        email.setFrom(emailSender);

        return email;
    }
//
}
