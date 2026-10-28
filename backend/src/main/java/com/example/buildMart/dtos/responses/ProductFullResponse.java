package com.example.buildMart.dtos.responses;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public record ProductFullResponse (
    Long id,
    String title,
    Double rating,
    BigDecimal price,
    Integer discount,
    String productCategory,
    String description,
    List<ProductImageResponse> images,
    List<TechnicalSpecificationFullResponse> specifications,
    Integer stockQuantity
) implements Serializable {}
