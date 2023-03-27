package com.zsoltnagy.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailConfig {
    @Value("${spring.mail.host}")
    String host;
    @Value("${spring.mail.port}")
    String port;
    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.mail.password}")
    String password;
    @Value("${spring.mail.protocol}")
    String protocol;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    String smtpTlsEnable;
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    String smtpTlsRequire;
    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    String smtpSslEnable;

}
