package com.semicolondevop.suite.service.email;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    public void send(String text, String subject, String to);

    public void sendSimpleMailMessage(SimpleMailMessage message);

    void sendWithSendGrid(SimpleMailMessage message) throws Exception;
}
