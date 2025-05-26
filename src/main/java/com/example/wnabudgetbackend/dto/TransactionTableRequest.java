package com.example.wnabudgetbackend.dto;

import com.example.wnabudgetbackend.model.Transaction;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransactionTableRequest {

    private UUID id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private String account_name;

    @Column(nullable = false)
    private String category_name;

    @Column(nullable = false)
    private BigDecimal amount; // positive = income, negative = expense

    private String payee;
    private String memo;

    private LocalDate date;

    private boolean cleared;

    public TransactionTableRequest(UUID id, UUID user_id, String account_name, String category_name, BigDecimal amount, String payee, String memo, boolean cleared, LocalDate date) {
        this.id = id;
        this.user_id = user_id;
        this.account_name = account_name;
        this.category_name = category_name;
        this.amount = amount;
        this.payee = payee;
        this.memo = memo;
        this.cleared = cleared;
        this.date = date;
    }

}