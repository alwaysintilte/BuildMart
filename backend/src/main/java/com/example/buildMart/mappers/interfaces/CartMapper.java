package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.CartResponse;
import com.example.buildMart.models.Cart;

public interface CartMapper {
    CartResponse toDto(Cart cart);
}
