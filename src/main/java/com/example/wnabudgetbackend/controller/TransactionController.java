package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.TransactionRequest;
import com.example.wnabudgetbackend.model.Transaction;
import com.example.wnabudgetbackend.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionRequest createTransaction(@RequestBody TransactionRequest transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<TransactionRequest>> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @GetMapping("/user/{userId}")
    public List<TransactionRequest> getTransactionsByUser(@PathVariable UUID userId) {
        return transactionService.getTransactionsByUser(userId);
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionRequest> getTransactionsByAccount(@PathVariable UUID accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionRequest> updateTransaction(@PathVariable UUID id, @RequestBody TransactionRequest updatedTx) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, updatedTx));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
