package com.example.wnabudgetbackend.controller;

import com.example.wnabudgetbackend.dto.CategoryGroupRequest;
import com.example.wnabudgetbackend.dto.patch.CategoryGroupPatch;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getGroupsByUser(@PathVariable UUID userId) {
        if (!securityUtil.isAuthorized(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(categoryGroupService.getGroupsByUser(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchGroup(
            @PathVariable UUID id,
            @RequestBody CategoryGroupPatch patch
    ) {
        if (patch.getUser_id() == null || !securityUtil.isAuthorized(patch.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(categoryGroupService.patchGroup(id, patch));
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
