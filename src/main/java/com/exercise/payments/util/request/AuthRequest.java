package com.exercise.payments.util.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
