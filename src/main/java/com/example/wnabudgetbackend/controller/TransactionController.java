package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.TransactionRequest;
import com.example.wnabudgetbackend.service.TransactionService;
import com.example.wnabudgetbackend.config.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SecurityUtil securityUtil;

    public TransactionController(TransactionService transactionService, SecurityUtil securityUtil) {
        this.transactionService = transactionService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transaction) {
        if (!securityUtil.isAuthorized(transaction.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(transaction));
    }

    @GetMapping
    public String test() {
        return "Hi";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable UUID id) {
        Optional<TransactionRequest> tx = transactionService.getTransaction(id);
        if (tx.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UUID userId = tx.get().getUser_id();
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(tx.get());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTransactionsByUser(@PathVariable UUID userId) {
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getTransactionsByAccount(@PathVariable UUID accountId) {
        UUID userId = transactionService.getAccountUserId(accountId); // You must implement this
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(transactionService.getTransactionsByAccount(accountId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable UUID id, @RequestBody TransactionRequest updatedTx) {
        if (!securityUtil.isAuthorized(updatedTx.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(transactionService.updateTransaction(id, updatedTx));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable UUID id) {
        Optional<TransactionRequest> transactionRequestOptional = transactionService.getTransaction(id);
        if (transactionRequestOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
        }
        if (!securityUtil.isAuthorized(transactionRequestOptional.get().getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}