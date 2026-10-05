package com.example.buildMart.dtos.responses;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
    List<CartItemResponse> items,
    PromoCodeResponse promoCodeResponse,
    BigDecimal totalPrice,
    BigDecimal finalPrice,
    Integer totalItems
) {}
