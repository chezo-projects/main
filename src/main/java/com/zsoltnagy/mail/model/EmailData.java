package com.zsoltnagy.mail.model;

import lombok.Data;

@Data
public class EmailData {
    private String email;
    private String name;
    private String message;
}
