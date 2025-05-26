package com.example.wnabudgetbackend.service;

import com.example.wnabudgetbackend.dto.CategoryRequest;
import com.example.wnabudgetbackend.dto.patch.CategoryPatch;
import com.example.wnabudgetbackend.model.Category;
import com.example.wnabudgetbackend.model.CategoryGroup;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.CategoryGroupRepository;
import com.example.wnabudgetbackend.repository.CategoryRepository;
import com.example.wnabudgetbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final CategoryGroupRepository groupRepo;
    private final AccountService accountService;

    public CategoryService(CategoryRepository categoryRepo,
                           UserRepository userRepo,
                           CategoryGroupRepository groupRepo, AccountService accountService) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.accountService = accountService;
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
        category.setMonth(request.getMonth());
        category.setYear(request.getYear());
        category.setAvailable(request.getBudgetedAmount().add(request.getActivity()));

        category = categoryRepo.save(category);
        return new CategoryRequest(category);
    }

    public Optional<CategoryRequest> getCategory(UUID id) {
        return categoryRepo.findById(id)
                .map(CategoryRequest::new);
    }

    public List<CategoryRequest> getCategoriesByUser(UUID userId) {
        return categoryRepo.findByUserIdOrderByNameAsc(userId).stream()
                .map(CategoryRequest::new)
                .collect(Collectors.toList());
    }

    public BigDecimal getMoneyLeftToAssign(UUID userId) {
        List<Category> categories = categoryRepo.findByUserId(userId);
        BigDecimal moneyLeft = accountService.getTotalMoneyInAccounts(userId);
        for (Category category : categories) {
            moneyLeft = moneyLeft.subtract(category.getBudgetedAmount());
        }
        return moneyLeft;
    }

    public CategoryRequest patchCategory(UUID id, CategoryPatch patch) {
        Category cat = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        // update ONLY if client sents a value
        if (patch.getName() != null)            cat.setName(patch.getName());
        if (patch.getBudgetedAmount() != null)  cat.setBudgetedAmount(patch.getBudgetedAmount());
        if (patch.getActivity() != null)        cat.setActivity(patch.getActivity());
        if (patch.getAvailable() != null)       cat.setAvailable(patch.getAvailable());
        else cat.setAvailable(cat.getBudgetedAmount().add(cat.getActivity()));

        cat = categoryRepo.save(cat);
        return new CategoryRequest(cat);
    }

    public void deleteCategory(UUID id) {
        categoryRepo.deleteById(id);
    }
}
