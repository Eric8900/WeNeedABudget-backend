package com.example.wnabudgetbackend.dto.patch;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryPatch {

    private String  name;
    private BigDecimal budgetedAmount;
    private BigDecimal activity;
    private BigDecimal available;
    private UUID user_id;
}