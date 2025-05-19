package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.TransactionRequest;
import com.example.wnabudgetbackend.model.*;
import com.example.wnabudgetbackend.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        tx.setDate(request.getDate());

        // Update account balance
        account.setBalance(account.getBalance().add(tx.getAmount()));
        accountRepository.save(account);

        // Update category activity and available
        category.setActivity(category.getActivity().add(tx.getAmount()));
        category.setAvailable(category.getAvailable().add(tx.getAmount()));
        categoryRepository.save(category);

        tx = transactionRepository.save(tx);
        return new TransactionRequest(tx);
    }

    public Optional<TransactionRequest> getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .map(TransactionRequest::new);
    }

    public List<TransactionRequest> getTransactionsByUser(UUID userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(TransactionRequest::new)
                .collect(Collectors.toList());
    }

    public List<TransactionRequest> getTransactionsByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(TransactionRequest::new)
                .collect(Collectors.toList());
    }

    public TransactionRequest updateTransaction(UUID id, TransactionRequest updatedTx) {
        userRepository.findById(updatedTx.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findById(updatedTx.getAccount_id())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Category category = categoryRepository.findById(updatedTx.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        tx.setAmount(updatedTx.getAmount());
        tx.setPayee(updatedTx.getPayee());
        tx.setMemo(updatedTx.getMemo());
        tx.setDate(updatedTx.getDate());
        tx.setCleared(updatedTx.isCleared());
        tx.setAccount(account);
        tx.setCategory(category);

        tx = transactionRepository.save(tx);
        return new TransactionRequest(tx);
    }

    public void deleteTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }
}