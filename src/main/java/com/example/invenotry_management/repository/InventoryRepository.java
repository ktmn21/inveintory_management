package com.example.invenotry_management.repository;


import com.example.invenotry_management.entitty.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findByProductId(Long productId);
    List<InventoryEntity> findByStockLevelLessThan(int threshold);
}
