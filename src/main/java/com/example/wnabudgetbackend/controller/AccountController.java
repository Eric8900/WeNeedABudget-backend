package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.AccountRequest;
import com.example.wnabudgetbackend.service.AccountService;
import com.example.wnabudgetbackend.config.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final SecurityUtil securityUtil;

    public AccountController(AccountService accountService, SecurityUtil securityUtil) {
        this.accountService = accountService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest request) {
        if (!securityUtil.isAuthorized(request.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAccountsByUser(@PathVariable UUID userId) {
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(accountService.getAccountsByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccountById(@PathVariable UUID id, @RequestBody AccountRequest request) {
        if (!securityUtil.isAuthorized(request.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(accountService.updateAccount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        Optional<AccountRequest> optionalAccountRequest = accountService.getAccount(id);
        if (optionalAccountRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
        if (!securityUtil.isAuthorized(optionalAccountRequest.get().getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        accountService.deleteAccount(id);
        return ResponseEntity.ok(Map.of("message", "Account deleted"));
    }
}