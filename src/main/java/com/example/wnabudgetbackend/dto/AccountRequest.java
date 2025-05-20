package com.example.wnabudgetbackend.dto;

import com.example.wnabudgetbackend.model.Account;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    public AccountRequest(Account account) {
        this.id = account.getId();
        this.user_id = account.getUser().getId();
        this.name = account.getName();
        this.type = account.getType();
        this.balance = account.getBalance();
    }
}