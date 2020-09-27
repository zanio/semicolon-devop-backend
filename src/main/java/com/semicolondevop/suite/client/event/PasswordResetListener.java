package com.semicolondevop.suite.client.event;

/* Aniefiok
 *created on 5/24/2020
 *inside the package */


import com.semicolondevop.suite.model.admin.Admin;
import com.semicolondevop.suite.model.developer.Developer;
import com.semicolondevop.suite.service.developer.DeveloperService;
import com.semicolondevop.suite.service.email.EmailService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("passwordReset")
@Setter
@Getter
public class PasswordResetListener implements ApplicationListener<OnPasswordResetEvent> {

    @Autowired
    @Qualifier("saver")
    DeveloperService developerServiceImp;

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
    public void onApplicationEvent(OnPasswordResetEvent onPasswordResetEvent) {
        if(onPasswordResetEvent.getApplicationUser().equals("ADMIN")){
            this.passwordResetMessageToAdmin(onPasswordResetEvent);
        }
        if(onPasswordResetEvent.getApplicationUser().equals("USER")){
            this.passwordResetMessageToSaver(onPasswordResetEvent);
        }

    }


    private void passwordResetMessageToAdmin( OnPasswordResetEvent event) {

        Admin user = event.getAdmin();
        this.setUrl(event.getAppUrl()+"/api/password/confirm?token="+event.getToken());
        this.setSubject("Password Reset Confirmation");
        this.setAdmin(user);
        String recipientAddress = this.getAdmin().getEmail();
        String content = "Hi "+this.getAdmin().getFirstname()+"\n\nYour Requested to change your password, Please follow the link to reset your password " +

                "\n\n using the  url below \n\n: "+this.getUrl()+"\n\n If you didn't request for a password reset please ignore this message";
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


    private void passwordResetMessageToSaver( OnPasswordResetEvent event) throws Exception {

        Developer user = event.getUser();
        this.setUrl(event.getAppUrl()+"/api/password/confirm?token="+event.getToken());
        this.setSubject("Password Reset Confirmation");
        this.setSaver(user);
        String recipientAddress = this.getSaver().getEmail();
        String content = "Hi "+this.getSaver().getFirstname()+"\n\nYour Requested to change your password, Please follow the link to reset your password " +

                "\n\n using the  url below \n\n: "+this.getUrl()+"\n\n If you didn't request for a password reset please ignore this message";
        final SimpleMailMessage email =
                constructEmailMessage(content,recipientAddress,event.getLocale());

        try {
            emailServiceImpl.sendWithSendGrid(email);
        }catch (RuntimeException re){
            throw re;
        }

    }


    private SimpleMailMessage constructEmailMessage(final String mess,
                                                    final String recipientAddress, final Locale content) {
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
}
