package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.CartItemResponse;
import com.example.buildMart.models.CartItem;

import java.math.BigDecimal;
import java.util.List;

public interface CartItemMapper {
    CartItemResponse toDto(CartItem cartItem);
    List<CartItemResponse> toDtoList(List<CartItem> cartItems);
}
