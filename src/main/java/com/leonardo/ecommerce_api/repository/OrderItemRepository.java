package com.leonardo.ecommerce_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leonardo.ecommerce_api.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findByOrder_Id(UUID orderId);

    List<OrderItem> findByProduct_Id(UUID productId);

    List<OrderItem> findByOrderIdAndProductId(UUID orderId, UUID productId);

    boolean existsByProduct_Id(UUID productId);

    boolean existsByOrder_Id(UUID orderId);
}
