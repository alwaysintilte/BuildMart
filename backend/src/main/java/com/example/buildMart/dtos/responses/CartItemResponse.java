package com.example.buildMart.dtos.responses;

import java.math.BigDecimal;

public record CartItemResponse(
    ProductResponse productResponse,
    Integer quantity,
    BigDecimal price
) {}
