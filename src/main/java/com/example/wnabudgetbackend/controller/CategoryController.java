package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.CategoryRequest;
import com.example.wnabudgetbackend.dto.patch.CategoryPatch;
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

    @GetMapping("/left-to-assign/{userId}")
    public ResponseEntity<?> getMoneyLeftToAssign(@PathVariable UUID userId) {
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(categoryService.getMoneyLeftToAssign(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchCategory(
            @PathVariable UUID id,
            @RequestBody CategoryPatch patch
    ) {

        if (patch.getUser_id() == null || !securityUtil.isAuthorized(patch.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(categoryService.patchCategory(id, patch));
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