package com.example.buildMart.dtos.responses;

import java.io.Serializable;


public record TechnicalSpecificationFullResponse(
    Long id,
    String name,
    String value
) implements Serializable {}
