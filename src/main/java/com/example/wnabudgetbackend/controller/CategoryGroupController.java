package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.CategoryGroupRequest;
import com.example.wnabudgetbackend.model.CategoryGroup;
import com.example.wnabudgetbackend.service.CategoryGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/category-groups")
public class CategoryGroupController {

    private final CategoryGroupService categoryGroupService;

    public CategoryGroupController(CategoryGroupService categoryGroupService) {
        this.categoryGroupService = categoryGroupService;
    }

    @PostMapping
    public CategoryGroupRequest createGroup(@RequestBody CategoryGroupRequest request) {
        return categoryGroupService.createGroup(request);
    }

    @GetMapping("/{id}")
    public Optional<CategoryGroupRequest> getGroup(@PathVariable UUID id) {
        return categoryGroupService.getGroup(id);
    }

    @GetMapping("/user/{userId}")
    public List<CategoryGroupRequest> getGroupsByUser(@PathVariable UUID userId) {
        return categoryGroupService.getGroupsByUser(userId);
    }

    @PutMapping()
    public CategoryGroupRequest updateGroup(@RequestBody CategoryGroupRequest request) {
        return categoryGroupService.updateGroup(request);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable UUID id) {
        categoryGroupService.deleteGroup(id);
    }
}