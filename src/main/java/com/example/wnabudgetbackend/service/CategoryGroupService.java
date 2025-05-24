package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.CategoryGroupRequest;
import com.example.wnabudgetbackend.dto.patch.CategoryGroupPatch;
import com.example.wnabudgetbackend.model.CategoryGroup;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.CategoryGroupRepository;
import com.example.wnabudgetbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        return categoryGroupRepo.findAllByUserIdOrderByNameAsc(userId).stream()
                .map(CategoryGroupRequest::new)
                .collect(Collectors.toList());
    }

    public CategoryGroupRequest patchGroup(UUID id, CategoryGroupPatch patch) {
        CategoryGroup grp = categoryGroupRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        if (patch.getName() != null) grp.setName(patch.getName());

        grp = categoryGroupRepo.save(grp);
        return new CategoryGroupRequest(grp);
    }

    public void deleteGroup(UUID id) {
        categoryGroupRepo.deleteById(id);
    }
}