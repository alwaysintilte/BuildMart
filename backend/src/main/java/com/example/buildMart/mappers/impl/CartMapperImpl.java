package com.example.buildMart.mappers.impl;

import com.example.buildMart.dtos.responses.CartItemResponse;
import com.example.buildMart.dtos.responses.CartResponse;
import com.example.buildMart.dtos.responses.PromoCodeResponse;
import com.example.buildMart.mappers.interfaces.CartItemMapper;
import com.example.buildMart.mappers.interfaces.CartMapper;
import com.example.buildMart.mappers.interfaces.PromoCodeMapper;
import com.example.buildMart.models.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CartMapperImpl implements CartMapper {

    private CartItemMapper cartItemMapper;

    private PromoCodeMapper promoCodeMapper;

    @Override
    public CartResponse toDto(Cart cart) {
        if(cart == null){
            return null;
        }
        List<CartItemResponse> cartItemResponses = cartItemMapper.toDtoList(cart.getItems());
        PromoCodeResponse promoCodeResponse = promoCodeMapper.toDto(cart.getPromoCode());
        BigDecimal totalPrice = cart
                .getItems()
                .stream()
                .map(cartItem -> cartItem
                        .getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
        BigDecimal finalPrice = Optional
                .ofNullable(cart.getPromoCode())
                .map(promoCode -> totalPrice.subtract(totalPrice
                                .multiply(BigDecimal.valueOf(promoCode.getDiscountPercentage()))
                                .divide(BigDecimal.valueOf(100))
                        )
                )
                .orElse(totalPrice);
        Integer totalItems = cart.getItems().size();
        CartResponse cartResponse = new CartResponse(cartItemResponses, promoCodeResponse, totalPrice, finalPrice, totalItems);
        return cartResponse;
    }
}
