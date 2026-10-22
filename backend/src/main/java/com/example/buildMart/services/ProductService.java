package com.example.buildMart.services;

import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.exceptions.types.BadRequestException;
import com.example.buildMart.exceptions.types.NotFoundException;
import com.example.buildMart.mappers.interfaces.ProductMapper;
import com.example.buildMart.models.Product;
import com.example.buildMart.repositories.ProductRepository;
import com.example.buildMart.specifications.ProductSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    private ProductMapper productMapper;

    @Cacheable(
            value = "products_catalog",
            key = "{#page, #limit, #search, #minPrice, #maxPrice, #minRating, #sortBy, #order}"
    )
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Integer page, Integer limit, String search, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, String sortBy, String order){
        if(minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0){
            throw new BadRequestException("min_price must be <= max_price");
        }
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Specification<Product> specification = Specification.allOf(
                ProductSpecifications.titleContains(search),
                ProductSpecifications.priceGreaterThanOrEqual(minPrice),
                ProductSpecifications.priceLessThanOrEqual(maxPrice),
                ProductSpecifications.ratingGreaterThanOrEqual(minRating)
        );
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<Long> ids = products.map(product -> product.getId()).toList();
        if(!ids.isEmpty()){
            productRepository.findAllWithImagesByIdsIn(ids);
            productRepository.findAllWithSpecificationsByIdsIn(ids);
        }
        Page<ProductResponse> productResponses = products.map(product -> productMapper.toDto(product));
        return productResponses;
    }

    @Transactional(readOnly = true)
    public Page<Product> findAllEntities(Integer page, Integer limit, String search, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, String sortBy, String order){
        if(minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0){
            throw new BadRequestException("min_price must be <= max_price");
        }
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Specification<Product> specification = Specification.allOf(
                ProductSpecifications.titleContains(search),
                ProductSpecifications.priceGreaterThanOrEqual(minPrice),
                ProductSpecifications.priceLessThanOrEqual(maxPrice),
                ProductSpecifications.ratingGreaterThanOrEqual(minRating)
        );
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<Long> ids = products.map(product -> product.getId()).toList();
        if(!ids.isEmpty()){
            productRepository.findAllWithImagesByIdsIn(ids);
            productRepository.findAllWithSpecificationsByIdsIn(ids);
        }
        return products;
    }

    @Caching(evict = {
            @CacheEvict(value = "product_item", key = "#id"),
            @CacheEvict(value = "products_catalog", allEntries = true),
            @CacheEvict(value = "products_discount", allEntries = true)
    })
    @Transactional
    public void deleteById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.deleteById(id);
    }

    @Cacheable(value = "product_item", key = "#id")
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        List<Long> ids = List.of(product.getId());
        productRepository.findAllWithImagesByIdsIn(ids);
        productRepository.findAllWithSpecificationsByIdsIn(ids);
        ProductResponse productResponse = productMapper.toDto(product);
        return productResponse;
    }

    @Transactional(readOnly = true)
    public Product findByIdEntity(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        List<Long> ids = List.of(product.getId());
        productRepository.findAllWithImagesByIdsIn(ids);
        productRepository.findAllWithSpecificationsByIdsIn(ids);
        return product;
    }

    @Cacheable(
            value = "products_discount",
            key = "{#page, #limit, #minDiscount, #sortBy, #order}"
    )
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

    @Transactional(readOnly = true)
    public Page<Product> findByDiscountEntity(Integer page, Integer limit, Integer minDiscount, String sortBy, String order){
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> products = productRepository.findByDiscountGreaterThan(minDiscount, pageable);
        List<Long> ids = products.map(product -> product.getId()).toList();
        if(!ids.isEmpty()){
            productRepository.findAllWithImagesByIdsIn(ids);
            productRepository.findAllWithSpecificationsByIdsIn(ids);
        }
        return products;
    }
}
