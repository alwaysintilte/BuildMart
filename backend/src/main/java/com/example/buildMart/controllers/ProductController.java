package com.example.buildMart.controllers;

import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.services.ProductService;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@Validated
public class ProductController {

    private ProductService productService;

    @GetMapping("/")
    public ResponseEntity<Page<ProductResponse>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 1, message = "page must be >= 1")
            Integer page,

            @RequestParam(defaultValue = "6")
            @Min(value = 1, message = "limit must be >= 1")
            @Max(value = 100, message = "limit must be <= 100")
            Integer limit,

            @RequestParam(required = false)
            @Size(max = 200, message = "search must be <= 200 chars")
            String search,

            @RequestParam(name = "min_price", required = false)
            @PositiveOrZero(message = "min_price must be >= 0")
            BigDecimal minPrice,

            @RequestParam(name = "max_price", required = false)
            @PositiveOrZero(message = "max_price must be >= 0")
            BigDecimal maxPrice,

            @RequestParam(name = "min_rating", required = false)
            @DecimalMin(value = "0.0", message = "min_rating must be >= 0")
            @DecimalMax(value = "5.0", message = "min_rating must be <= 5")
            Double minRating,

            @RequestParam(name = "sort_by", defaultValue = "title")
            @Pattern(regexp = "price|title|rating", message = "sort_by must be one of: price, title, rating")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE, message = "order must be asc or desc")
            String order
    ){
        return new ResponseEntity<>(productService.findAll(page, limit, search, minPrice, maxPrice, minRating, sortBy, order), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id){
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/discount")
    public ResponseEntity<Page<ProductResponse>> findByDiscount(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "page must be >= 0")
            Integer page,

            @RequestParam(defaultValue = "6")
            @Min(value = 1, message = "limit must be >= 1")
            @Max(value = 100, message = "limit must be <= 100")
            Integer limit,

            @RequestParam(name = "min_discount", defaultValue = "0")
            @Min(value = 0, message = "min_discount must be >= 0")
            @Max(value = 100, message = "min_discount must be <= 100")
            Integer minDiscount,

            @RequestParam(name = "sort_by", defaultValue = "title")
            @Pattern(regexp = "price|title|rating", message = "sort_by must be one of: price, title, rating")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE, message = "order must be asc or desc")
            String order
    ){
        return new ResponseEntity<>(productService.findByDiscount(page, limit, minDiscount, sortBy, order), HttpStatus.OK);
    }
}
