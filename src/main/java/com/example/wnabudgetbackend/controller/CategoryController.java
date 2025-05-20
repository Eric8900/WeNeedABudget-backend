package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.CategoryRequest;
import com.example.wnabudgetbackend.service.CategoryService;
import com.example.wnabudgetbackend.config.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final SecurityUtil securityUtil;

    public CategoryController(CategoryService categoryService, SecurityUtil securityUtil) {
        this.categoryService = categoryService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        if (!securityUtil.isAuthorized(request.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable UUID id) {
        Optional<CategoryRequest> category = categoryService.getCategory(id);
        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UUID userId = category.get().getUser_id();
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(category.get());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCategoriesByUser(@PathVariable UUID userId) {
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(categoryService.getCategoriesByUser(userId));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getCategoriesByGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(categoryService.getCategoriesByGroup(groupId));
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryRequest request) {
        if (!securityUtil.isAuthorized(request.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(categoryService.updateCategory(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id) {
        Optional<CategoryRequest> categoryRequestOptional = categoryService.getCategory(id);
        if (categoryRequestOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        if (!securityUtil.isAuthorized(categoryRequestOptional.get().getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}