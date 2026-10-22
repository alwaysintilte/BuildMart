package com.example.buildMart.controllers;

import com.example.buildMart.dtos.requests.CartItemPatchRequest;
import com.example.buildMart.dtos.requests.CartItemPostRequest;
import com.example.buildMart.dtos.responses.CartPromoCodeResponse;
import com.example.buildMart.dtos.responses.CartResponse;
import com.example.buildMart.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
@Validated
@Tag(
        name = "Cart",
        description = "Управление корзиной пользователя и применение промокодов"
)
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private CartService cartService;

    @Operation(
            summary = "Получить корзину",
            description = "Возвращает корзину текущего авторизованного пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Корзина получена"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "404", description = "Корзина не найдена")
    })
    @GetMapping("/")
    public ResponseEntity<CartResponse> getCart(Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.getCart(email),HttpStatus.OK);
    }

    @Operation(
            summary = "Добавить товар в корзину",
            description = "Добавляет товар или увеличивает его количество"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Товар добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "404", description = "Товар не найден"),
            @ApiResponse(responseCode = "409", description = "Недостаточно товара на складе")
    })
    @PostMapping("/items")
    public ResponseEntity<CartResponse> postItem(
            @Valid @RequestBody CartItemPostRequest cartItemPostRequest,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.postItem(cartItemPostRequest, email), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Изменить количество товара",
            description = "Обновляет количество товара в корзине"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Количество обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "404", description = "Товар отсутствует в корзине"),
            @ApiResponse(responseCode = "409", description = "Недостаточно товара на складе")
    })
    @PatchMapping("/items/{id}")
    public ResponseEntity<CartResponse> patchItem(
            @Valid @RequestBody CartItemPatchRequest cartItemPatchRequest,
            @PathVariable @Positive Long productId,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.patchItem(cartItemPatchRequest, productId, email), HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить товар из корзины",
            description = "Удаляет указанный товар из корзины пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар удалён"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "404", description = "Товар отсутствует в корзине")
    })
    @DeleteMapping("items/{id}")
    public ResponseEntity<CartResponse> deleteItem(
            @PathVariable @Positive Long productId,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.deleteItem(productId, email), HttpStatus.OK);
    }

    @Operation(
            summary = "Применить промокод",
            description = "Применяет промокод к корзине и пересчитывает стоимость"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Промокод применён"),
            @ApiResponse(responseCode = "400", description = "Промокод неактивен"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "404", description = "Промокод или корзина не найдены"),
            @ApiResponse(responseCode = "409", description = "Промокод уже применён")
    })
    @PostMapping("/apply-promo")
    public ResponseEntity<CartPromoCodeResponse> applyPromo(
            @RequestBody @NotBlank String promoCode,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.applyPromo(promoCode, email), HttpStatus.OK);
    }
}
