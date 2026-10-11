package com.example.buildMart.dtos.responses;

import java.io.Serializable;

public record TechnicalSpecificationResponse(
    String name,
    String value
) implements Serializable {}
