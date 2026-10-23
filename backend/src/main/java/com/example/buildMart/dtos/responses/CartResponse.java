package com.example.buildMart.dtos.responses;

import java.util.List;

public record CartResponse(
    List<CartItemResponse> items,
    PromoCodeResponse promoCodeResponse,
    PriceResponse priceResponse,
    Integer totalItems
) {}
