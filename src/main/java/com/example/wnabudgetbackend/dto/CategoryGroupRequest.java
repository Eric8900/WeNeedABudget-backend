package com.example.wnabudgetbackend.dto;

import com.example.wnabudgetbackend.model.CategoryGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
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
