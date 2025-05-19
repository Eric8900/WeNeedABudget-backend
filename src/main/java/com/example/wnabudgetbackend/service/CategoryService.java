package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.CategoryRequest;
import com.example.wnabudgetbackend.model.Category;
import com.example.wnabudgetbackend.model.CategoryGroup;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.CategoryGroupRepository;
import com.example.wnabudgetbackend.repository.CategoryRepository;
import com.example.wnabudgetbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public CategoryRequest createCategory(CategoryRequest request) {
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

        category = categoryRepo.save(category);
        return new CategoryRequest(category);
    }

    public Optional<CategoryRequest> getCategory(UUID id) {
        return categoryRepo.findById(id)
                .map(CategoryRequest::new);
    }

    public List<CategoryRequest> getCategoriesByUser(UUID userId) {
        return categoryRepo.findByUserId(userId).stream()
                .map(CategoryRequest::new)
                .collect(Collectors.toList());
    }

    public List<CategoryRequest> getCategoriesByGroup(UUID groupId) {
        return categoryRepo.findByGroupId(groupId).stream()
                .map(CategoryRequest::new)
                .collect(Collectors.toList());
    }

    public CategoryRequest updateCategory(CategoryRequest request) {
        Category category = categoryRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setBudgetedAmount(request.getBudgetedAmount());
        category.setActivity(request.getActivity());
        category.setAvailable(request.getBudgetedAmount().add(request.getActivity()));

        category = categoryRepo.save(category);
        return new CategoryRequest(category);
    }

    public void deleteCategory(UUID id) {
        categoryRepo.deleteById(id);
    }
}
