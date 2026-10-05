package com.example.buildMart.services;

import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.mappers.interfaces.ProductMapper;
import com.example.buildMart.models.Product;
import com.example.buildMart.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    private ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Integer page, Integer limit, String search, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, String sortBy, String order){
        if(minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0){
            throw new IllegalArgumentException("min_price must be <= max_price");
        }
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> products = productRepository.findAll(search, minPrice, maxPrice, minRating, pageable);
        List<Long> ids = products.map(product -> product.getId()).toList();
        if(!ids.isEmpty()){
            productRepository.findAllWithImagesByIdsIn(ids);
            productRepository.findAllWithSpecificationsByIdsIn(ids);
        }
        Page<ProductResponse> productResponses = products.map(product -> productMapper.toDto(product));
        return productResponses;
    }

    public void deleteById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        productRepository.deleteById(id);
    }

    public ProductResponse findById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        ProductResponse productResponse = productMapper.toDto(product);
        return productResponse;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findByDiscount(Integer page, Integer limit, Integer minDiscount, String sortBy, String order){
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> products = productRepository.findByDiscountGreaterThan(minDiscount, pageable);
        List<Long> ids = products.map(product -> product.getId()).toList();
        if(!ids.isEmpty()){
            productRepository.findAllWithImagesByIdsIn(ids);
            productRepository.findAllWithSpecificationsByIdsIn(ids);
        }
        Page<ProductResponse> productResponses = products.map(product -> productMapper.toDto(product));
        return productResponses;
    }
}
