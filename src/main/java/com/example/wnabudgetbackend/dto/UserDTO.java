package com.example.wnabudgetbackend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    String token;
    UUID user_id;
}
