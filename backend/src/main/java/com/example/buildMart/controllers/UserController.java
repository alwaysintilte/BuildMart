package com.example.buildMart.controllers;

import com.example.buildMart.dtos.requests.UserAuthRequest;
import com.example.buildMart.dtos.responses.JwtTokenResponse;
import com.example.buildMart.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(
        name = "Authentication",
        description = "Регистрация и авторизация пользователей"
)
public class UserController {

    private UserService userService;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создаёт нового пользователя с ролью CUSTOMER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserAuthRequest userAuthRequest){
        userService.registerUser(userAuthRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = "Проверяет email и пароль, возвращает JWT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Авторизация успешна"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Неверный email или пароль")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> loginUser(@Valid @RequestBody UserAuthRequest userAuthRequest){
        return new ResponseEntity<>(userService.loginUser(userAuthRequest), HttpStatus.CREATED);
    }
}
