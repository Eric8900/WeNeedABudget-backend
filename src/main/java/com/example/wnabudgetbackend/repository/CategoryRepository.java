package com.example.wnabudgetbackend.repository;

import com.example.wnabudgetbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserId(UUID userId);
    List<Category> findByUserIdOrderByNameAsc(UUID userId);
}
