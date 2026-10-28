package com.example.buildMart.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalSpecificationRequest {

    private Long id;

    @NotBlank(message = "Specification name cannot be empty")
    @Size(max = 256, message = "Specification name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Specification value cannot be empty")
    @Size(max = 256, message = "Specification value cannot exceed 255 characters")
    private String value;
}
