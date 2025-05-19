package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.AccountRequest;
import com.example.wnabudgetbackend.model.Account;
import com.example.wnabudgetbackend.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account createAccount(@RequestBody AccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public Optional<Account> getAccount(@PathVariable UUID id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/user/{userId}")
    public List<Account> getAccountsByUser(@PathVariable UUID userId) {
        return accountService.getAccountsByUser(userId);
    }

    @PutMapping()
    public Account updateAccount(@RequestBody AccountRequest request) {
        return accountService.updateAccount(request.getId(), request);
    }

    @PutMapping("/{id}")
    public Account updateAccountById(@PathVariable UUID id, @RequestBody AccountRequest request) {
        return accountService.updateAccount(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
    }
}