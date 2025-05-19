package com.example.wnabudgetbackend.dto;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest {

    private UUID id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // "checking", "savings", etc.

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

}