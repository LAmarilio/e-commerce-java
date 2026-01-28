package com.leonardo.ecommerce_api.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDTO {
    private UUID userId;
    private List<OrderItemRequestDTO> items;
}
