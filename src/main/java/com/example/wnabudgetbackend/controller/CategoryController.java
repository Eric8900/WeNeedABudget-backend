package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.CategoryRequest;
import com.example.wnabudgetbackend.model.Category;
import com.example.wnabudgetbackend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategory(@PathVariable UUID id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/user/{userId}")
    public List<Category> getCategoriesByUser(@PathVariable UUID userId) {
        return categoryService.getCategoriesByUser(userId);
    }

    @GetMapping("/group/{groupId}")
    public List<Category> getCategoriesByGroup(@PathVariable UUID groupId) {
        return categoryService.getCategoriesByGroup(groupId);
    }

    @PutMapping
    public Category updateCategory(@RequestBody CategoryRequest request) {
        return categoryService.updateCategory(request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
    }
}