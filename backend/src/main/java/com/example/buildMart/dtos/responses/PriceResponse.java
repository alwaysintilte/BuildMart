package com.example.buildMart.dtos.responses;

import java.math.BigDecimal;

public record PriceResponse(
        BigDecimal totalPrice,
        BigDecimal finalPrice
) {}
