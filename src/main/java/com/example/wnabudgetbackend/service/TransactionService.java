package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.TransactionRequest;
import com.example.wnabudgetbackend.dto.TransactionTableRequest;
import com.example.wnabudgetbackend.model.*;
import com.example.wnabudgetbackend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public TransactionRequest createTransaction(TransactionRequest request) {
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
        tx.setDate(LocalDate.now());

        if (tx.isCleared()) {
            // Update account balance if CLEARED
            account.setBalance(account.getBalance().add(tx.getAmount()));
            accountRepository.save(account);

            // Update category activity and available if CLEARED
            category.setActivity(category.getActivity().add(tx.getAmount()));
            category.setAvailable(category.getAvailable().add(tx.getAmount()));
            categoryRepository.save(category);
        }

        tx = transactionRepository.save(tx);
        return new TransactionRequest(tx);
    }

    public Optional<TransactionRequest> getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .map(TransactionRequest::new);
    }

    public List<TransactionTableRequest> getTransactionsByAccountAndUserId(UUID accountId, UUID userId) {
        List<Transaction> transactions = transactionRepository.findByAccountIdAndUserIdOrderByAmountAsc(accountId, userId);
        List<TransactionTableRequest> ret = new ArrayList<>();
        for (Transaction transaction : transactions) {
            ret.add(new TransactionTableRequest(
                    transaction.getId(),
                    transaction.getUser().getId(),
                    transaction.getAccount().getName(),
                    transaction.getCategory().getName(),
                    transaction.getAmount(),
                    transaction.getPayee(),
                    transaction.getMemo(),
                    transaction.isCleared(),
                    transaction.getDate()
            ));
        }
        return ret;
    }

    public TransactionRequest updateTransactionChecked(UUID id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        Account account = accountRepository.findById(tx.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Category category = categoryRepository.findById(tx.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        tx.setDate(LocalDate.now());
        tx.setCleared(!tx.isCleared());

        if (tx.isCleared()) {
            // Update account balance if CLEARED
            account.setBalance(account.getBalance().add(tx.getAmount()));
            accountRepository.save(account);

            // Update category activity and available if CLEARED
            category.setActivity(category.getActivity().add(tx.getAmount()));
            category.setAvailable(category.getAvailable().add(tx.getAmount()));
            categoryRepository.save(category);
        }
        else {
            // Update account balance if CLEARED
            account.setBalance(account.getBalance().subtract(tx.getAmount()));
            accountRepository.save(account);

            // Update category activity and available if CLEARED
            category.setActivity(category.getActivity().subtract(tx.getAmount()));
            category.setAvailable(category.getAvailable().subtract(tx.getAmount()));
            categoryRepository.save(category);
        }

        tx = transactionRepository.save(tx);
        return new TransactionRequest(tx);
    }

    public void deleteTransaction(UUID id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (tx.isCleared()) {
            Account account = accountRepository.findById(tx.getAccount().getId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            Category category = categoryRepository.findById(tx.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            // Update account balance if CLEARED
            account.setBalance(account.getBalance().subtract(tx.getAmount()));
            accountRepository.save(account);

            // Update category activity and available if CLEARED
            category.setActivity(category.getActivity().subtract(tx.getAmount()));
            category.setAvailable(category.getAvailable().subtract(tx.getAmount()));
            categoryRepository.save(category);
        }
        transactionRepository.deleteById(id);
    }

}