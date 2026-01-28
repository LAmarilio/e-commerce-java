package com.leonardo.ecommerce_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.ecommerce_api.dto.OrderRequestDTO;
import com.leonardo.ecommerce_api.dto.OrderResponseDTO;
import com.leonardo.ecommerce_api.dto.ProductResponseDTO;
import com.leonardo.ecommerce_api.model.Order;
import com.leonardo.ecommerce_api.model.OrderStatus;
import com.leonardo.ecommerce_api.repository.OrderRepository;
import com.leonardo.ecommerce_api.repository.UserRepository;
import com.leonardo.ecommerce_api.model.OrderItem;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;

    public void verifyUserId(OrderRequestDTO dto) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new RuntimeException("O usuário com este ID não foi localizado!");
        }
    }

    public void verifyListItemsNotEmpty(OrderRequestDTO dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new RuntimeException("O pedido deve conter ao menos um item!");
        }
    }

    public void verifyStockAndUnitValue(OrderRequestDTO dto) {
        dto.getItems().forEach(item -> {
            Optional<ProductResponseDTO> productOpt = productService.getProductById(item.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("O produto com este ID não foi encontrado!");
            }
            if (productOpt.get().getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("A quantidade solicitada do produto " + productOpt.get().getName()
                        + " é maior do que a disponível em estoque!");
            }
            if (productOpt.get().getPrice() == null) {
                throw new RuntimeException("O produto com este ID não possui um preço definido!");
            }
            if (!item.getUnitValue().equals(productOpt.get().getPrice())) {
                throw new RuntimeException("O valor unitário do item não corresponde ao valor unitário do produto!");
            }
        });
    }

    public void validateOrderCreation(OrderRequestDTO dto) {
        verifyUserId(dto);
        verifyListItemsNotEmpty(dto);
        verifyStockAndUnitValue(dto);
    }

    public boolean isEditable(OrderStatus status) {
        if (status.equals(OrderStatus.PENDING)) {
            return true;
        }
        throw new RuntimeException("Este pedido já não pode mais ser alterado!");
    }

    public BigDecimal calculateTotalAmount(OrderRequestDTO dto) {
        BigDecimal total = dto.getItems().stream()
                .map(item -> item.getUnitValue().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    public OrderResponseDTO toResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setItems(order.getItems()
                .stream()
                .map(orderItemService::toResponseDTO)
                .toList());

        return dto;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        validateOrderCreation(dto);
        BigDecimal totalAmount = calculateTotalAmount(dto);

        Order newOrder = new Order();
        newOrder.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("O usuário com este ID não foi localizado!")));
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setTotalAmount(totalAmount);

        dto.getItems().forEach(itemDto -> {
            OrderItem orderItem = orderItemService.buildEntityFromDTO(newOrder, itemDto);
            newOrder.getItems().add(orderItem);
        });

        Order saved = repo.save(newOrder);
        return toResponseDTO(saved);
    }

    public OrderResponseDTO getOrderById(UUID orderId) {
        Optional<Order> orderOpt = repo.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Pedido não encontrado!");
        }
        return toResponseDTO(orderOpt.get());
    }

    @Transactional
    public OrderResponseDTO paidOrder(UUID orderId) {
        Order order = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        if (order.getStatus().equals(OrderStatus.PENDING)) {
            order.setStatus(OrderStatus.PAID);
            order.getItems().forEach(item -> {
                productService.consumeStock(item.getProduct().getId(), item.getQuantity());
            });
            Order updated = repo.save(order);
            return toResponseDTO(updated);
        } else {
            throw new RuntimeException("Apenas pedidos com status PENDING podem ser pagos!");
        }
    }

    @Transactional
    public OrderResponseDTO cancelOrder(UUID orderId) {
        Order order = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        if (order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.PAID)) {
            if (order.getStatus().equals(OrderStatus.PAID)) {
                order.getItems().forEach(item -> {
                    productService.restock(item.getProduct().getId(), item.getQuantity());
                });
            }
            order.setStatus(OrderStatus.CANCELED);
            Order updated = repo.save(order);
            return toResponseDTO(updated);
        } else {
            throw new RuntimeException("Apenas pedidos com status PENDING podem ser cancelados!");
        }
    }

    @Transactional
    public OrderResponseDTO shippedOrder(UUID orderId) {
        Order order = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        if (order.getStatus().equals(OrderStatus.PAID)) {
            order.setStatus(OrderStatus.SHIPPED);
            Order updated = repo.save(order);
            return toResponseDTO(updated);
        } else {
            throw new RuntimeException("Apenas pedidos com status PAID podem ser enviados!");
        }
    }

    @Transactional
    public OrderResponseDTO updateOrderItems(UUID orderId, OrderRequestDTO dto) {
        Order order = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        isEditable(order.getStatus());
        validateOrderCreation(dto);
        BigDecimal totalAmount = calculateTotalAmount(dto);

        order.getItems().clear();
        dto.getItems().forEach(itemDto -> {
            OrderItem orderItem = orderItemService.buildEntityFromDTO(order, itemDto);
            order.getItems().add(orderItem);
        });

        order.setTotalAmount(totalAmount);
        Order updated = repo.save(order);

        return toResponseDTO(updated);
    }

    public List<OrderResponseDTO> getAllOrdersByUserId(UUID userId) {
        List<Order> orders = repo.findByUser_Id(userId);
        return orders.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}