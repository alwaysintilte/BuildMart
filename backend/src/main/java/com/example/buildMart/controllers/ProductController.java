package com.example.buildMart.controllers;

import com.example.buildMart.models.Cart;
import com.example.buildMart.models.Category;
import com.example.buildMart.models.Product;
import com.example.buildMart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/")
    public Page<Product> findAllByPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String sort){
        return productService.findAllByPage(page, size, sort);
    }

    @GetMapping("/id/{id}")
    public Optional<Product> findById(@PathVariable String id){
        return  productService.findById(id);
    }

    @GetMapping("/categories")
    public List<Category> findAllCategories(){
        return productService.findAllCategories();
    }

    @GetMapping("/discount")
    public List<Product> findByDiscount(){
        return  productService.findByDiscount();
    }

    @GetMapping("/params")
    public Page<Product> findAllByParams(@RequestParam(required = false) Float rating, @RequestParam(required = false) Float minPrice, @RequestParam(required = false) Float maxPrice, @RequestParam(required = false) String category, @RequestParam Integer page, @RequestParam Integer size, @RequestParam String sort){
        return productService.findAllByParams(rating, minPrice, maxPrice, category, page, size, sort);
    }
}
