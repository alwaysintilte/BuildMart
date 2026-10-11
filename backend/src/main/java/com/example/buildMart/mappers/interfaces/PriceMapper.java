package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.PriceResponse;
import com.example.buildMart.models.Cart;

public interface PriceMapper {

    PriceResponse toDto(Cart cart);
}
