package com.leonardo.ecommerce_api.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponseDTO {
    private UUID id;
    private UUID productId;
    private int quantity;
    private BigDecimal unitValue;
    private BigDecimal subtotal;
}
