package com.leonardo.ecommerce_api.repository;

import com.leonardo.ecommerce_api.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceLessThan(BigDecimal price);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
}