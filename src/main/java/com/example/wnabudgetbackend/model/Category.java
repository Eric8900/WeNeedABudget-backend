package com.example.wnabudgetbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private CategoryGroup group;

    @Column(nullable = false)
    private String name;

    private BigDecimal budgetedAmount = BigDecimal.ZERO;
    private BigDecimal activity = BigDecimal.ZERO;
    private BigDecimal available = BigDecimal.ZERO;

}