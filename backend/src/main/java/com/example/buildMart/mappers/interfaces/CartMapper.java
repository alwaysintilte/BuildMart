package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.CartPromoCodeResponse;
import com.example.buildMart.dtos.responses.CartResponse;
import com.example.buildMart.models.Cart;

public interface CartMapper {
    CartResponse toDto(Cart cart);

    CartPromoCodeResponse toPromoCodeDto(Cart cart);
}
