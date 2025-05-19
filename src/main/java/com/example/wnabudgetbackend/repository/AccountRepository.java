package com.example.wnabudgetbackend.repository;

import com.example.wnabudgetbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUserId(UUID userId);
}
