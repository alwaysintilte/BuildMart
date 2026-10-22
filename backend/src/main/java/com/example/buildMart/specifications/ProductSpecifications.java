package com.example.buildMart.specifications;

import com.example.buildMart.models.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

public class ProductSpecifications {

    public static Specification<Product> titleContains(String search) {
        Specification<Product> specification = Optional.ofNullable(search)
                .filter(value -> !value.isBlank())
                .map(value -> (Specification<Product>) (root, query, cb) ->
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + value.toLowerCase(Locale.ROOT) + "%"
                        ))
                .orElseGet(() -> Specification.unrestricted());
        return specification;
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        Specification<Product> specification = Optional.ofNullable(minPrice)
                .map(value -> (Specification<Product>) (root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get("price"), value))
                .orElseGet(() -> Specification.unrestricted());
        return specification;
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        Specification<Product> specification = Optional.ofNullable(maxPrice)
                .map(value -> (Specification<Product>) (root, query, cb) ->
                        cb.lessThanOrEqualTo(root.get("price"), value))
                .orElseGet(() -> Specification.unrestricted());
        return specification;
    }

    public static Specification<Product> ratingGreaterThanOrEqual(Double minRating) {
        Specification<Product> specification = Optional.ofNullable(minRating)
                .map(value -> (Specification<Product>) (root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get("rating"), value))
                .orElseGet(() -> Specification.unrestricted());
        return specification;
    }
}