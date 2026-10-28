package com.example.buildMart.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPatchRequest {
    @Size(min = 3, max = 255, message = "Title must contain between 3 and 255 characters")
    private String title;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot exceed 100%")
    private Integer discount;

    private String productCategory;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @Valid
    private List<ProductImageRequest> images;

    @Valid
    private List<TechnicalSpecificationRequest> specifications;

    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
}