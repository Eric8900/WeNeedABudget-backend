package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.AccountRequest;
import com.example.wnabudgetbackend.model.Account;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.AccountRepository;
import com.example.wnabudgetbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(AccountRequest request) {
        if (request.getId() != null && accountRepository.existsById(request.getId())) {
            throw new RuntimeException("Account with this ID already exists.");
        }
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setName(request.getName());
        account.setType(request.getType());
        account.setBalance(request.getBalance());

        return accountRepository.save(account);
    }

    public Optional<Account> getAccount(UUID id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAccountsByUser(UUID userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account updateAccount(UUID id, AccountRequest request) {
        userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return accountRepository.findById(id).map(account -> {
            account.setName(request.getName());
            account.setType(request.getType());
            account.setBalance(request.getBalance());

            return accountRepository.save(account);
        }).orElseThrow();
    }

    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }
}