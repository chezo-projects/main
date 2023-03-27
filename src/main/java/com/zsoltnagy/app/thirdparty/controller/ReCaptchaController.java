package com.zsoltnagy.app.thirdparty.controller;

import com.zsoltnagy.app.thirdparty.service.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recaptcha")
@CrossOrigin(origins = {"chezo.hu", "zsolt-nagy.com", "https://chezo.hu", "https://zsolt-nagy.com", "http://localhost:3000"})
public class ReCaptchaController {

    private final RecaptchaValidator recaptchaValidator;

    @Autowired
    public ReCaptchaController(RecaptchaValidator recaptchaValidator) {
        this.recaptchaValidator = recaptchaValidator;
    }

    @PostMapping("/verify")
    public boolean verifyReCaptcha(@RequestBody String token) {
        return recaptchaValidator.isValid(token);
    }
}
