package com.leonardo.ecommerce_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.ecommerce_api.dto.OrderRequestDTO;
import com.leonardo.ecommerce_api.dto.OrderResponseDTO;
import com.leonardo.ecommerce_api.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByUserId(@PathVariable UUID userId) {
        List<OrderResponseDTO> orders = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO items) {
        OrderResponseDTO order = orderService.createOrder(items);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/update-paid/{id}")
    public ResponseEntity<OrderResponseDTO> orderPaid(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.paidOrder(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
    }

    @PutMapping("/update-canceled/{id}")
    public ResponseEntity<OrderResponseDTO> orderCanceled(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.cancelOrder(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
    }

    @PutMapping("/update-shipped/{id}")
    public ResponseEntity<OrderResponseDTO> orderShipped(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.shippedOrder(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
    }

    @PutMapping("/update-order/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable UUID id, @Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO order = orderService.updateOrderItems(id, dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
    }
}
