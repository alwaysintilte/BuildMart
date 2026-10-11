package com.example.buildMart.mappers.impl;

import com.example.buildMart.dtos.responses.*;
import com.example.buildMart.mappers.interfaces.CartItemMapper;
import com.example.buildMart.mappers.interfaces.CartMapper;
import com.example.buildMart.mappers.interfaces.PriceMapper;
import com.example.buildMart.mappers.interfaces.PromoCodeMapper;
import com.example.buildMart.models.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CartMapperImpl implements CartMapper {

    private CartItemMapper cartItemMapper;

    private PromoCodeMapper promoCodeMapper;

    private PriceMapper priceMapper;

    @Override
    public CartResponse toDto(Cart cart) {
        if(cart == null){
            return new CartResponse(null, null, null, 0);
        }
        List<CartItemResponse> cartItemResponses = cartItemMapper.toDtoList(cart.getItems());
        PromoCodeResponse promoCodeResponse = promoCodeMapper.toDto(cart.getPromoCode());
        PriceResponse priceResponse = priceMapper.toDto(cart);
        Integer totalItems = cart.getItems().stream().mapToInt(cartItem -> cartItem.getQuantity()).sum();
        CartResponse cartResponse = new CartResponse(cartItemResponses, promoCodeResponse, priceResponse, totalItems);
        return cartResponse;
    }

    @Override
    public CartPromoCodeResponse toPromoCodeDto(Cart cart) {
        if(cart == null){
            return new CartPromoCodeResponse(null, null);
        }
        PromoCodeResponse promoCodeResponse = promoCodeMapper.toDto(cart.getPromoCode());
        PriceResponse priceResponse = priceMapper.toDto(cart);
        CartPromoCodeResponse cartResponse = new CartPromoCodeResponse(promoCodeResponse, priceResponse);
        return cartResponse;
    }
}
