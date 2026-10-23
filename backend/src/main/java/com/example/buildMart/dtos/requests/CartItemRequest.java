package com.example.buildMart.dtos.requests;

public record CartItemRequest(
    Long productId,
    Integer quantity
) {}
