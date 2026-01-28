package com.leonardo.ecommerce_api.repository;

import com.leonardo.ecommerce_api.model.Order;
import com.leonardo.ecommerce_api.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUser_Id(UUID userId);

    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);

    boolean existsByUser_Id(UUID userId);

    boolean existsById(UUID orderId);
}
