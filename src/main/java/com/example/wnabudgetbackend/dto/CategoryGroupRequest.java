package com.example.wnabudgetbackend.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CategoryGroupRequest {

    private UUID id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private String name;

}
