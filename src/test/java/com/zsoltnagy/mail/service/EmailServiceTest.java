package com.zsoltnagy.mail.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "messageFrom", "test@test.com");
    }

    @Test
    void sendMessagesTest() {
        // Arrange
        String email = "test1@test.com";
        String name = "John";
        String messageContent = "Test message";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test@test.com");
        message.setTo(email);
        message.setSubject("Contact with Zsolt Nagy - Software Engineer");
        message.setText("Dear John!\n\nThanks for your email, I've got it with this message:\n\"Test message\"\n\nI will reply to your message soon, thank you for your patience\n\nBest wishes,\nZsolt Nagy");

        mailSender.send(any(SimpleMailMessage.class));

        // Act
        emailService.sendMessages(email, name, messageContent);

        // Assert
        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
    }
}