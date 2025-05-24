package com.example.wnabudgetbackend.dto;

import com.example.wnabudgetbackend.model.Category;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CategoryRequest {

    private UUID id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private UUID group_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    private BigDecimal budgetedAmount = BigDecimal.ZERO;
    private BigDecimal activity = BigDecimal.ZERO;
    private BigDecimal available = BigDecimal.ZERO;

    public CategoryRequest(Category category) {
        this.id = category.getId();
        this.user_id = category.getUser().getId();
        this.group_id = category.getGroup().getId();
        this.name = category.getName();
        this.month = category.getMonth();
        this.year = category.getYear();
        this.budgetedAmount = category.getBudgetedAmount();
        this.activity = category.getActivity();
        this.available = category.getAvailable();
    }
}
