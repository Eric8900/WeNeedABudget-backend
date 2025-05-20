package com.example.wnabudgetbackend.dto;

import com.example.wnabudgetbackend.model.CategoryGroup;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CategoryGroupRequest {

    private UUID id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private String name;

    public CategoryGroupRequest(CategoryGroup categoryGroup) {
        this.id = categoryGroup.getId();
        this.name = categoryGroup.getName();
        this.user_id = categoryGroup.getUser().getId();
    }
}
