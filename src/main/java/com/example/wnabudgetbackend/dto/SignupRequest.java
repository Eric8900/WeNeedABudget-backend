package com.example.wnabudgetbackend.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
}