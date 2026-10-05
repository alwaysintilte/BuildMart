package com.example.buildMart.dtos.responses;

public record PromoCodeResponse(
    String code,
    Integer discountPercentage
) {}
