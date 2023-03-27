package com.zsoltnagy.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    String messageFrom;

    private final JavaMailSender javaMailSender;

    public void sendMessages(String email, String name, String messageContent) {
        String verificationMessage = """
                <html>
                <head>
                <style>
                body {background-color: #2D3543;}
                h1   {color: #A2A1A1;}
                p    {color: red;}
                .msgHolder  { padding 1rem; border-radius:10px;background-color: #2D3543; ; color: #A2A1A1}
                .yourmessage {color: gray; background-color:  #213543;  margin:1rem;  border-radius: 10px;  padding: 10px}
                .signature {}
                </style>
                </head>
                <body>
                <div class="msgHolder">
                <h2>Dear %s!</h2>
                <h3>
                Thanks for your email, I've got it with this message:
                </h3>
                <div class="yourmessage">
                <h4>
                "%s"
                </h4></div>
                <h3>
                I will reply to your message soon, thank you for your patience.
                </h3>
                <div class="signature">
                <h2>
                Best wishes:<br />
                Zsolt Nagy<br />
                Software Engineer
                </h2>
                </div>
                </div>
                </body>
                </html>
                """.formatted(name, messageContent);

        String messageToMe = String.format("%s sent a message from %s with this content:\n\n%n%s", name, email, messageContent);
        log.info("got message request");
        if (sendMessage(email, "Contact with Zsolt Nagy - Software Engineer", verificationMessage)) {

            sendMessage("vallalkozas@chezo.hu", String.format("Contact form: %s - %s", name, email), messageToMe);
        }

    }

    private boolean sendMessage(String emailTo, String subject, String messageContent) {
        MimeMessage messagee = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(messagee, true, "UTF-8");
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setFrom(messageFrom);
            helper.setText(messageContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        log.info("trying to send message");
        try {
            javaMailSender.send(messagee);
            log.info("Email sent to this address: {}", emailTo);
            return true;
        } catch (MailException e) {
            log.error("Exception on the email sending process: {} {}", emailTo, e.getMessage());
            return false;
        }
    }


}
