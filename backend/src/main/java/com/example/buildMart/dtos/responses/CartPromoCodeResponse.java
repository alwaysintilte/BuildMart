package com.example.buildMart.dtos.responses;

public record CartPromoCodeResponse(
    PromoCodeResponse promoCodeResponse,
    PriceResponse priceResponse
) {}
