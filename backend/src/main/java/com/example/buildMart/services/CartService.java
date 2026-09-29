package com.example.buildMart.services;

import com.example.buildMart.models.Cart;
import com.example.buildMart.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {
    private ProductService productService;
    @Autowired
    public CartService(ProductService productService){
        this.productService = productService;
    }
    public Cart getCart(Map<String, Integer> cartItems){
        Cart cart = new Cart();
        for (String productId: cartItems.keySet()) {
            Product product = productService.findById(productId).get();
            cart.addProduct(product,cartItems.get(productId));
        }
        return cart;
    }
}
