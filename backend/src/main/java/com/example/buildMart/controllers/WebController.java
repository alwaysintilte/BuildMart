package com.example.buildMart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping("/product.html")
    public String productPage(@RequestParam String id) {
        return "forward:/pages/product.html";
    }
    @GetMapping("")
    public String indexPage() {
        return "forward:/pages/index.html";
    }
    @GetMapping("/cart.html")
    public String cartPage() {
        return "forward:/pages/cart.html";
    }
    @GetMapping("/about.html")
    public String aboutPage() {
        return "forward:/pages/about.html";
    }
    @GetMapping("/categories.html")
    public String categoriesPage() {
        return "forward:/pages/categories.html";
    }
    @GetMapping("/deals.html")
    public String dealsPage() {
        return "forward:/pages/deals.html";
    }
}
