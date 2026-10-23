package com.example.buildMart.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 256, message = "Password must be between 8 and 256 characters")
    private String password;
}
