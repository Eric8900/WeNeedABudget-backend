package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.AccountRequest;
import com.example.wnabudgetbackend.dto.TransactionTableRequest;
import com.example.wnabudgetbackend.model.Account;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.AccountRepository;
import com.example.wnabudgetbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    public AccountRequest createAccount(AccountRequest request) {
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

        account = accountRepository.save(account);
        return new AccountRequest(account);
    }

    public Optional<AccountRequest> getAccount(UUID id) {
        return accountRepository.findById(id)
                .map(AccountRequest::new);
    }

    public List<AccountRequest> getAccountsByUser(UUID userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(AccountRequest::new)
                .collect(Collectors.toList());
    }

    public AccountRequest updateAccount(UUID id, AccountRequest request) {
        userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setName(request.getName());
        account.setType(request.getType());
        account.setBalance(request.getBalance());

        account = accountRepository.save(account);
        return new AccountRequest(account);
    }

    public void deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<TransactionTableRequest> transactions = transactionService.getTransactionsByAccountAndUserId(id, account.getUser().getId());
        for (TransactionTableRequest transactionTableRequest : transactions) {
            transactionService.deleteTransaction(transactionTableRequest.getId());
        }

        accountRepository.deleteById(id);
    }

    // HELPERS
    public BigDecimal getTotalMoneyInAccounts(UUID id) {
        List<Account> accounts = accountRepository.findByUserId(id);
        BigDecimal totalMoney = BigDecimal.ZERO;
        for (Account account : accounts) {
            totalMoney = totalMoney.add(account.getBalance());
        }
        return totalMoney;
    }
}