package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.CategoryGroupRequest;
import com.example.wnabudgetbackend.model.CategoryGroup;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.CategoryGroupRepository;
import com.example.wnabudgetbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepo;
    private final UserRepository userRepo;

    public CategoryGroupService(CategoryGroupRepository categoryGroupRepo, UserRepository userRepo) {
        this.categoryGroupRepo = categoryGroupRepo;
        this.userRepo = userRepo;
    }

    public CategoryGroupRequest createGroup(CategoryGroupRequest request) {
        if (request.getId() != null && categoryGroupRepo.existsById(request.getId())) {
            throw new RuntimeException("Category group with this ID already exists.");
        }
        User user = userRepo.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CategoryGroup group = new CategoryGroup();
        group.setUser(user);
        group.setName(request.getName());

        group = categoryGroupRepo.save(group);
        return new CategoryGroupRequest(group);
    }

    public Optional<CategoryGroupRequest> getGroup(UUID id) {
        return categoryGroupRepo.findById(id)
                .map(CategoryGroupRequest::new);
    }

    public List<CategoryGroupRequest> getGroupsByUser(UUID userId) {
        return categoryGroupRepo.findByUserId(userId).stream()
                .map(CategoryGroupRequest::new)
                .collect(Collectors.toList());
    }

    public CategoryGroupRequest updateGroup(CategoryGroupRequest request) {
        userRepo.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CategoryGroup group = categoryGroupRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Category group not found"));

        group.setName(request.getName());

        group = categoryGroupRepo.save(group);
        return new CategoryGroupRequest(group);
    }

    public void deleteGroup(UUID id) {
        categoryGroupRepo.deleteById(id);
    }
}