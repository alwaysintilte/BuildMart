package com.example.buildMart.mappers.impl;

import com.example.buildMart.dtos.responses.CartItemResponse;
import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.mappers.interfaces.CartItemMapper;
import com.example.buildMart.mappers.interfaces.ProductMapper;
import com.example.buildMart.models.CartItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class CartItemMapperImpl implements CartItemMapper {
    private ProductMapper productMapper;
    @Override
    public CartItemResponse toDto(CartItem cartItem) {
        if(cartItem == null){
            return null;
        }
        ProductResponse productResponse = productMapper.toDto(cartItem.getProduct());
        Integer quantity = cartItem.getQuantity();
        BigDecimal price = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        CartItemResponse cartItemResponse = new CartItemResponse(productResponse, quantity, price);
        return cartItemResponse;
    }

    @Override
    public List<CartItemResponse> toDtoList(List<CartItem> cartItems) {
        if(cartItems == null){
            return null;
        }
        List<CartItemResponse> list = cartItems.stream().map(cartItem -> toDto(cartItem)).toList();
        return list;
    }
}
