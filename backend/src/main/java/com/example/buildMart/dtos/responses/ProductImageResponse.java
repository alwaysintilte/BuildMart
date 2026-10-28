package com.example.buildMart.dtos.responses;

import java.io.Serializable;

public record ProductImageResponse (
    Long id,
    String url
) implements Serializable {}
