package com.example.buildMart.dtos.responses;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
    Long id,
    String title,
    Double rating,
    BigDecimal price,
    Integer discount,
    String productCategory,
    String description,
    List<String> images,
    List<TechnicalSpecificationResponse> specifications
) {}
