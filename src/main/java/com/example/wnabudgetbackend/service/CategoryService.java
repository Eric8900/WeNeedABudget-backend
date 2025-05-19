package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.CategoryRequest;
import com.example.wnabudgetbackend.model.Category;
import com.example.wnabudgetbackend.model.CategoryGroup;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.AccountRepository;
import com.example.wnabudgetbackend.repository.CategoryGroupRepository;
import com.example.wnabudgetbackend.repository.CategoryRepository;
import com.example.wnabudgetbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final CategoryGroupRepository groupRepo;

    public CategoryService(CategoryRepository categoryRepo,
                           UserRepository userRepo,
                           CategoryGroupRepository groupRepo) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
    }

    public Category createCategory(CategoryRequest request) {
        if (request.getId() != null && categoryRepo.existsById(request.getId())) {
            throw new RuntimeException("Category with this ID already exists.");
        }
        User user = userRepo.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CategoryGroup group = groupRepo.findById(request.getGroup_id())
                .orElseThrow(() -> new RuntimeException("Category group not found"));

        Category category = new Category();
        category.setUser(user);
        category.setGroup(group);
        category.setName(request.getName());
        category.setBudgetedAmount(request.getBudgetedAmount());
        category.setActivity(request.getActivity());
        category.setAvailable(request.getBudgetedAmount().add(request.getActivity()));
        return categoryRepo.save(category);
    }

    public Optional<Category> getCategory(UUID id) {
        return categoryRepo.findById(id);
    }

    public List<Category> getCategoriesByUser(UUID userId) {
        return categoryRepo.findByUserId(userId);
    }

    public List<Category> getCategoriesByGroup(UUID groupId) {
        return categoryRepo.findByGroupId(groupId);
    }

    public Category updateCategory(CategoryRequest request) {
        return categoryRepo.findById(request.getId()).map(category -> {
            category.setName(request.getName());
            category.setBudgetedAmount(request.getBudgetedAmount());
            category.setActivity(request.getActivity());
            category.setAvailable(request.getBudgetedAmount().add(request.getActivity()));
            return categoryRepo.save(category);
        }).orElseThrow();
    }

    public void deleteCategory(UUID id) {
        categoryRepo.deleteById(id);
    }
}