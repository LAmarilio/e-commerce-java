package com.leonardo.ecommerce_api.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leonardo.ecommerce_api.dto.OrderItemRequestDTO;
import com.leonardo.ecommerce_api.dto.OrderItemResponseDTO;
import com.leonardo.ecommerce_api.model.Order;
import com.leonardo.ecommerce_api.model.OrderItem;
import com.leonardo.ecommerce_api.model.Product;
import com.leonardo.ecommerce_api.repository.ProductRepository;

@Service
public class OrderItemService {
    @Autowired
    private ProductRepository productRepository;

    public OrderItemResponseDTO toResponseDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitValue(item.getUnitValue());
        dto.setSubtotal(item.getUnitValue().multiply(BigDecimal.valueOf(item.getQuantity())));
        return dto;
    }

    public Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
    }

    public OrderItem buildEntityFromDTO(Order order, OrderItemRequestDTO dto) {
        Product product = getProduct(dto.getProductId());
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        item.setUnitValue(dto.getUnitValue());
        return item;
    }

    public void updateEntityFromDTO(OrderItem item, OrderItemRequestDTO dto) {
        Product product = getProduct(dto.getProductId());
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        item.setUnitValue(dto.getUnitValue());
    }
}