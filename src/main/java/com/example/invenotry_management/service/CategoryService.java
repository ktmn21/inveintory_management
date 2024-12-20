package com.example.invenotry_management.service;

import com.example.invenotry_management.dto.CategoryDTO;
import com.example.invenotry_management.entitty.CategoryEntity;
import com.example.invenotry_management.exception.ResourceNotFoundException;
import com.example.invenotry_management.repository.CategoryRepository;
import com.example.invenotry_management.repository.ProductRepository;
import com.example.invenotry_management.entitty.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    // Create a new category
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);

        categoryDTO.setId(category.getId());
        return categoryDTO;
    }

    // Get all categories
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> {
                    CategoryDTO dto = new CategoryDTO();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    dto.setProductIds(
                            category.getProducts() != null ?
                                    category.getProducts().stream().map(ProductEntity::getId).collect(Collectors.toList()) :
                                    null
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Get a category by ID
    public CategoryDTO getCategoryById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setProductIds(
                category.getProducts() != null ?
                        category.getProducts().stream().map(ProductEntity::getId).collect(Collectors.toList()) :
                        null
        );
        return dto;
    }

    // Update a category
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);

        categoryDTO.setId(category.getId());
        return categoryDTO;
    }

    public void deleteCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete a category with associated products.");
        }

        categoryRepository.delete(category);
    }
}
