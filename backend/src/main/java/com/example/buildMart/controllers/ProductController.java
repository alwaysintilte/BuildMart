package com.example.buildMart.controllers;

import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Products",
        description = "Каталог товаров, поиск, фильтрация и управление товарами"
)
public class ProductController {

    private ProductService productService;

    @Operation(
            summary = "Получить каталог товаров",
            description = "Возвращает товары с пагинацией, поиском, фильтрацией и сортировкой"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Каталог получен"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @GetMapping("/")
    public ResponseEntity<Page<ProductResponse>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "page must be >= 0")
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

    @Operation(
            summary = "Удалить товар",
            description = "Удаляет товар по ID. Доступно только ADMIN"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар удалён"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Товар не найден"),
            @ApiResponse(responseCode = "409", description = "Удаление нарушает ограничения базы данных")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Positive Long id){
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Получить товар по ID",
            description = "Возвращает товар, его изображения и характеристики"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар найден"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable @Positive Long id){
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить товары со скидкой",
            description = "Возвращает товары с фильтрацией по минимальной скидке"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товары получены"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
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
