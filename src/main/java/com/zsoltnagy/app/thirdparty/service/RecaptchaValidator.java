package com.zsoltnagy.app.thirdparty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class RecaptchaValidator {

    private final String RECAPTCHA_SECRET_KEY;
    private static final String RECAPTCHA_VERIFICATION_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final double MIN_SCORE = 0.3;

    @Autowired
    private RecaptchaValidator(@Value("${google-recaptcha-secret}") String recaptcha_secret_key) {
        RECAPTCHA_SECRET_KEY = recaptcha_secret_key;
    }

    private byte[] getRequestParams(JsonObject myToken) {
        String requestParams = "secret=" + RECAPTCHA_SECRET_KEY + "&response=" + myToken.getString("token");
        return requestParams.getBytes(StandardCharsets.UTF_8);
    }

    public boolean isValid(String token) {
        try {

            JsonObject myToken = Json.createReader(new StringReader(token)).readObject();

            URL url = new URL(RECAPTCHA_VERIFICATION_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(getRequestParams(myToken));
            }

            JsonObject result = readResponse(connection);

            return result.getBoolean("success") && result.getJsonNumber("score").doubleValue() > MIN_SCORE;

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private static JsonObject readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
        }
        return Json.createReader(new StringReader(responseBuilder.toString())).readObject();
    }

}
