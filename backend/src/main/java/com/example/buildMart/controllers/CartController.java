package com.example.buildMart.controllers;

import com.example.buildMart.dtos.requests.CartItemPatchRequest;
import com.example.buildMart.dtos.requests.CartItemPostRequest;
import com.example.buildMart.dtos.responses.CartPromoCodeResponse;
import com.example.buildMart.dtos.responses.CartResponse;
import com.example.buildMart.services.CartService;
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
public class CartController {

    private CartService cartService;

    @GetMapping("/")
    public ResponseEntity<CartResponse> getCart(Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.getCart(email),HttpStatus.OK);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> postItem(
            @Valid @RequestBody CartItemPostRequest cartItemPostRequest,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.postItem(cartItemPostRequest, email), HttpStatus.CREATED);
    }

    @PatchMapping("/items/{id}")
    public ResponseEntity<CartResponse> patchItem(
            @Valid @RequestBody CartItemPatchRequest cartItemPatchRequest,
            @PathVariable @Positive Long productId,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.patchItem(cartItemPatchRequest, productId, email), HttpStatus.OK);
    }

    @DeleteMapping("items/{id}")
    public ResponseEntity<CartResponse> deleteItem(
            @PathVariable @Positive Long productId,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.deleteItem(productId, email), HttpStatus.OK);
    }

    @PostMapping("/apply-promo")
    public ResponseEntity<CartPromoCodeResponse> applyPromo(
            @RequestBody @NotBlank String promoCode,
            Authentication authentication
    ){
        String email = authentication.getName();
        return new ResponseEntity<>(cartService.applyPromo(promoCode, email), HttpStatus.OK);
    }
}
