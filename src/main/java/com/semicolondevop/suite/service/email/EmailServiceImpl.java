package com.semicolondevop.suite.service.email;


/*
 *@author Aniefiok Akpan
 * created on 07/05/2020
 *
 */

import com.semicolondevop.suite.util.RetryOnException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    SendGrid sendGrid;

    @Override
    public void send(String text, String subject, String to) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendSimpleMailMessage(SimpleMailMessage message) {

        emailSender.send(message);
    }

    @Override
    public void sendWithSendGrid(SimpleMailMessage message) throws Exception {

        Email from = new Email(message.getFrom());
        String subject = message.getSubject();
        Email to = new Email(Objects.requireNonNull(message.getTo())[0]);
        Content content = new Content("text/plain", message.getText());
        Mail mail = new Mail(from, subject, to, content);

        RetryOnException retryHandler = new RetryOnException();

        while(true) {

            Request request = new Request();
            Response response = null;
            int getStatus;
            try {

                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                log.info("Email message request --> {" + request.getHeaders()
                        +"\n"+request.getBody() +"}");

                 response = sendGrid.api(request);

                log.info("Email message response --> {" + response.getHeaders()
                        +"\n"+response.getStatusCode()+"\n"+response.getBody() +"}");

            }
            // Catch exception and retry.
            // If beyond retry limit, this will throw an exception.
            catch (Exception ex)
            {
                retryHandler.exceptionOccurred();
                continue;
            }

            getStatus = response.getStatusCode();
            log.info("The status code:=>{}",response.getStatusCode());
            if(getStatus == 202)
            {
                break;
            }
        }


    }

    }




