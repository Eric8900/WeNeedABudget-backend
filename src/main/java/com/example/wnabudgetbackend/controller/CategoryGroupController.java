package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.CategoryGroupRequest;
import com.example.wnabudgetbackend.service.CategoryGroupService;
import com.example.wnabudgetbackend.config.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/category-groups")
public class CategoryGroupController {

    private final CategoryGroupService categoryGroupService;
    private final SecurityUtil securityUtil;

    public CategoryGroupController(CategoryGroupService categoryGroupService, SecurityUtil securityUtil) {
        this.categoryGroupService = categoryGroupService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CategoryGroupRequest request) {
        if (!securityUtil.isAuthorized(request.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryGroupService.createGroup(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable UUID id) {
        Optional<CategoryGroupRequest> group = categoryGroupService.getGroup(id);
        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UUID userId = group.get().getUser_id();
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(group.get());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getGroupsByUser(@PathVariable UUID userId) {
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(categoryGroupService.getGroupsByUser(userId));
    }

    @PutMapping
    public ResponseEntity<?> updateGroup(@RequestBody CategoryGroupRequest request) {
        if (!securityUtil.isAuthorized(request.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(categoryGroupService.updateGroup(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable UUID id) {
        Optional<CategoryGroupRequest> groupRequestOptional = categoryGroupService.getGroup(id);
        if (groupRequestOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category group not found");
        }
        if (!securityUtil.isAuthorized(groupRequestOptional.get().getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        categoryGroupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
