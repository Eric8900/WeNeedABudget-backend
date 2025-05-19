package com.example.wnabudgetbackend.dto;

import com.example.wnabudgetbackend.model.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequest {

    private UUID id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private UUID account_id;

    @Column(nullable = false)
    private UUID category_id;

    @Column(nullable = false)
    private BigDecimal amount; // positive = income, negative = expense

    private String payee;
    private String memo;

    private LocalDate date;

    private boolean cleared;

    public TransactionRequest(Transaction transactionRequest) {
        id = transactionRequest.getId();
        user_id = transactionRequest.getUser().getId();
        account_id = transactionRequest.getAccount().getId();
        category_id = transactionRequest.getCategory().getId();
        amount = transactionRequest.getAmount();
        payee = transactionRequest.getPayee();
        memo = transactionRequest.getMemo();
        date = transactionRequest.getDate();
        cleared = transactionRequest.isCleared();
    }

}
