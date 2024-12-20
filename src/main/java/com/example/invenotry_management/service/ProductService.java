package com.example.invenotry_management.service;


import com.example.invenotry_management.dto.ProductDTO;
import com.example.invenotry_management.entitty.CategoryEntity;
import com.example.invenotry_management.entitty.InventoryEntity;
import com.example.invenotry_management.entitty.ProductEntity;
import com.example.invenotry_management.exception.ResourceNotFoundException;
import com.example.invenotry_management.repository.CategoryRepository;
import com.example.invenotry_management.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create a new product
    public ProductDTO createProduct(ProductDTO productDTO) {
        logger.info("Creating new product: {}", productDTO.getName());

        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));

        ProductEntity product = new ProductEntity();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);

        product = productRepository.save(product);

        productDTO.setId(product.getId());
        return productDTO;
    }

    // Get all products
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get a product by ID
    public ProductDTO getProductById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return convertToDTO(product);
    }

    // Update a product
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        logger.info("Updating product with ID: {}", id);

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);

        product = productRepository.save(product);

        return convertToDTO(product);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    public ProductDTO getProductByName(String name) {
        ProductEntity product = productRepository.findByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with name: " + name));

        // Map ProductEntity and InventoryEntity to ProductDTO
        Long categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        int stockLevel = product.getInventory() != null ? product.getInventory().getStockLevel() : 0;

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                categoryId,
                stockLevel
        );
    }


    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        List<ProductEntity> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found for category ID: " + categoryId);
        }

        // Map ProductEntity to ProductDTO
        return products.stream().map(product -> {
            Long catId = product.getCategory() != null ? product.getCategory().getId() : null;
            int stockLevel = product.getInventory() != null ? product.getInventory().getStockLevel() : 0;

            return new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    catId,
                    stockLevel
            );
        }).toList();
    }

    //low-stock products report
    public List<ProductDTO> getLowStockProducts(int threshold) {
        List<ProductEntity> lowStockProducts = productRepository.findByStockLevelLessThan(threshold);

        if (lowStockProducts.isEmpty()) {
            throw new ResourceNotFoundException("No low-stock products found.");
        }

        // Map ProductEntity to ProductDTO
        return lowStockProducts.stream().map(product -> {
            Long catId = product.getCategory() != null ? product.getCategory().getId() : null;
            int stockLevel = product.getInventory() != null ? product.getInventory().getStockLevel() : 0;

            return new ProductDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    catId,
                    stockLevel
            );
        }).toList();
    }




    // Helper method to convert ProductEntity to ProductDTO
    private ProductDTO convertToDTO(ProductEntity product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategory().getId());

        // If inventory exists, set the stock level
        InventoryEntity inventory = product.getInventory();
        dto.setStockLevel(inventory != null ? inventory.getStockLevel() : null);

        return dto;
    }
}

