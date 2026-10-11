package com.example.buildMart.mappers.impl;

import com.example.buildMart.dtos.responses.PriceResponse;
import com.example.buildMart.mappers.interfaces.PriceMapper;
import com.example.buildMart.models.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PriceMapperImpl implements PriceMapper {

    @Override
    public PriceResponse toDto(Cart cart) {
        if(cart == null){
            return new PriceResponse(BigDecimal.ZERO, BigDecimal.ZERO);
        }
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
        PriceResponse priceResponse = new PriceResponse(totalPrice, finalPrice);
        return priceResponse;
    }
}
