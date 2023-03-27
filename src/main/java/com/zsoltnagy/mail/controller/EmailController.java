package com.zsoltnagy.mail.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsoltnagy.mail.model.EmailData;
import com.zsoltnagy.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@CrossOrigin(origins = {"chezo.hu", "zsolt-nagy.com", "https://chezo.hu", "https://zsolt-nagy.com", "http://localhost:3000"})
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    @ResponseBody
    public ResponseEntity<String> email(@RequestBody String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EmailData emailData = mapper.readValue(json, EmailData.class);
        emailService.sendMessages(emailData.getEmail(), emailData.getName(), emailData.getMessage());
        return ResponseEntity.ok().build();
    }
}