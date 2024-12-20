package com.example.invenotry_management.repository;

import com.example.invenotry_management.entitty.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryId(Long categoryId);

    // Method to search for a product by name
    Optional<ProductEntity> findByNameContainingIgnoreCase(String name);

    // Custom query to find products with low stock
    @Query("SELECT p FROM ProductEntity p JOIN p.inventory i WHERE i.stockLevel < :threshold")
    List<ProductEntity> findByStockLevelLessThan(@Param("threshold") int threshold);
}

