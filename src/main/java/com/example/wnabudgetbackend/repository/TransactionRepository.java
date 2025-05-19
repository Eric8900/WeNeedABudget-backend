package com.example.wnabudgetbackend.repository;

import com.example.wnabudgetbackend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserId(UUID userId);
    List<Transaction> findByAccountId(UUID accountId);
}
