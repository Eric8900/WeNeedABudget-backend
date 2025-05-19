package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.TransactionRequest;
import com.example.wnabudgetbackend.model.*;
import com.example.wnabudgetbackend.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              AccountRepository accountRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction createTransaction(TransactionRequest request) {
        if (request.getId() != null && transactionRepository.existsById(request.getId())) {
            throw new RuntimeException("Transaction with this ID already exists.");
        }
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountRepository.findById(request.getAccount_id())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Category category = categoryRepository.findById(request.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAccount(account);
        tx.setCategory(category);
        tx.setAmount(request.getAmount());
        tx.setPayee(request.getPayee());
        tx.setMemo(request.getMemo());
        tx.setCleared(request.isCleared());

        BigDecimal newBalance = account.getBalance().add(tx.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        BigDecimal newActivity = category.getActivity().add(tx.getAmount());
        BigDecimal newAvailable = category.getAvailable().add(tx.getAmount());
        category.setActivity(newActivity);
        category.setAvailable(newAvailable);
        categoryRepository.save(category);

        return transactionRepository.save(tx);
    }

    public Optional<Transaction> getTransaction(UUID id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByUser(UUID userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction updateTransaction(UUID id, TransactionRequest updatedTx) {
        userRepository.findById(updatedTx.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountRepository.findById(updatedTx.getAccount_id())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Category category = categoryRepository.findById(updatedTx.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return transactionRepository.findById(id).map(tx -> {
            tx.setAmount(updatedTx.getAmount());
            tx.setPayee(updatedTx.getPayee());
            tx.setMemo(updatedTx.getMemo());
            tx.setDate(updatedTx.getDate());
            tx.setCleared(updatedTx.isCleared());
            tx.setAccount(account);
            tx.setCategory(category);
            return transactionRepository.save(tx);
        }).orElseThrow();
    }

    public void deleteTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }
}