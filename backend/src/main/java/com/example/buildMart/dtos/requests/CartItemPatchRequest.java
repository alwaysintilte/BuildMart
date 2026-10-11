package com.example.buildMart.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemPatchRequest {
    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be >= 1")
    @Max(value = 10000, message = "quantity must be <= 10000")
    private Integer quantity;
}
