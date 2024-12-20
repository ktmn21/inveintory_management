package com.example.invenotry_management.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    private List<Long> productIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
