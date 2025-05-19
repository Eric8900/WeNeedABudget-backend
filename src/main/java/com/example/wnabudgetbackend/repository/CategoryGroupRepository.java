package com.example.wnabudgetbackend.repository;

import com.example.wnabudgetbackend.model.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, UUID> {
    List<CategoryGroup> findByUserId(UUID userId);
}
