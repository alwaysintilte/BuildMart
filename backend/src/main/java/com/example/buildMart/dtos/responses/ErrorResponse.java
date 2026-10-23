package com.example.buildMart.dtos.responses;

public record ErrorResponse(
    Integer code,
    String message,
    String error
) {}
