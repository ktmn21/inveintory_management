package com.example.invenotry_management.controller;

import com.example.invenotry_management.dto.ProductDTO;
import com.example.invenotry_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Create Product
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        productService.createProduct(productDTO);
        return ResponseEntity.ok("Product created successfully");
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductDTO productDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        productService.updateProduct(id, productDTO);
        return ResponseEntity.ok("Product updated successfully");
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to search for a product by name
    @GetMapping("/search")
    public ProductDTO searchProductByName(@RequestParam("name") String name) {
        return productService.getProductByName(name);
    }

    // Endpoint to filter products by category
    @GetMapping("/filter")
    public ResponseEntity<List<ProductDTO>> filterProductsByCategory(@RequestParam Long categoryId) {
        List<ProductDTO> productDTOs = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/low-stock/{threshold}")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts(@PathVariable int threshold) {

        List<ProductDTO> lowStockProducts = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(lowStockProducts);

    }

}

