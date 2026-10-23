package com.example.buildMart.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartPromoCodeRequest {

    @NotBlank
    private String promoCode;
}