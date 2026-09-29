package com.example.buildMart.controllers;

import com.example.buildMart.models.Cart;
import com.example.buildMart.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }
    @PostMapping("/products")
    public Cart getCart(@RequestBody Map<String, Integer> cartItems){
        return cartService.getCart(cartItems);
    }
}
