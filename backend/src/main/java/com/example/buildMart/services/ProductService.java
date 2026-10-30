package com.example.buildMart.services;

import com.example.buildMart.dtos.requests.ProductImageRequest;
import com.example.buildMart.dtos.requests.ProductPatchRequest;
import com.example.buildMart.dtos.requests.ProductPostRequest;
import com.example.buildMart.dtos.responses.ProductFullResponse;
import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.exceptions.types.BadRequestException;
import com.example.buildMart.exceptions.types.NotFoundException;
import com.example.buildMart.mappers.interfaces.ProductMapper;
import com.example.buildMart.models.Product;
import com.example.buildMart.models.ProductImage;
import com.example.buildMart.models.TechnicalSpecification;
import com.example.buildMart.models.enums.ProductCategory;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    private ProductMapper productMapper;

    @Cacheable(
            value = "products_catalog",
            key = "{#page, #limit, #search, #category, #minPrice, #maxPrice, #minRating, #sortBy, #order}"
    )
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Integer page, Integer limit, String search, String category, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, String sortBy, String order){
        if(minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0){
            throw new BadRequestException("min_price must be <= max_price");
        }
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Specification<Product> specification = Specification.allOf(
                ProductSpecifications.titleContains(search),
                ProductSpecifications.categoryEquals(category),
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

    @Cacheable(
            value = "products_catalog_admin",
            key = "{#page, #limit, #search, #category, #minPrice, #maxPrice, #minRating, #sortBy, #order}"
    )
    @Transactional(readOnly = true)
    public Page<ProductFullResponse> findAllFull(Integer page, Integer limit, String search, String category, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, String sortBy, String order){
        if(minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0){
            throw new BadRequestException("min_price must be <= max_price");
        }
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Specification<Product> specification = Specification.allOf(
                ProductSpecifications.titleContains(search),
                ProductSpecifications.categoryEquals(category),
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
        Page<ProductFullResponse> productResponses = products.map(product -> productMapper.toFullDto(product));
        return productResponses;
    }

    @Caching(evict = {
            @CacheEvict(value = "product_item", key = "#id"),
            @CacheEvict(value = "product_item_admin", key = "#id"),
            @CacheEvict(value = "products_catalog", allEntries = true),
            @CacheEvict(value = "products_catalog_admin", allEntries = true),
            @CacheEvict(value = "products_discount", allEntries = true)
    })
    @Transactional
    public void deleteById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Caching(evict = {
            @CacheEvict(value = "products_catalog", allEntries = true),
            @CacheEvict(value = "products_catalog_admin", allEntries = true),
            @CacheEvict(value = "products_discount", allEntries = true)
    })
    @Transactional
    public ProductFullResponse postProduct(ProductPostRequest productPostRequest){
        Product product = productMapper.toEntity(productPostRequest);
        Optional.ofNullable(productPostRequest.getProductCategory())
                .map(category -> ProductCategory.fromLabel(category))
                .ifPresent(category -> product.setProductCategory(category));
        Optional.ofNullable(productPostRequest.getImages())
                .map(
                        imageList -> imageList.stream()
                                    .map(imageRequest -> {
                                        ProductImage image = new ProductImage();
                                        image.setUrl(imageRequest.getUrl());
                                        image.setProduct(product);
                                        return image;
                                    })
                                    .toList()
                )
                .ifPresent(imagesList -> product.setImages(imagesList));
        Optional.ofNullable(productPostRequest.getSpecifications())
                .map(
                        technicalSpecificationList -> technicalSpecificationList.stream()
                                    .map(technicalSpecificationRequest -> {
                                        TechnicalSpecification specification = new TechnicalSpecification();
                                        specification.setName(technicalSpecificationRequest.getName());
                                        specification.setValue(technicalSpecificationRequest.getValue());
                                        specification.setProduct(product);
                                        return specification;
                                    })
                                    .toList()
                )
                .ifPresent(technicalSpecificationList -> product.setSpecifications(technicalSpecificationList));
        Product savedProduct = productRepository.save(product);
        ProductFullResponse productResponse = productMapper.toFullDto(savedProduct);
        return productResponse;
    }

    @Caching(evict = {
            @CacheEvict(value = "product_item", key = "#id"),
            @CacheEvict(value = "product_item_admin", key = "#id"),
            @CacheEvict(value = "products_catalog", allEntries = true),
            @CacheEvict(value = "products_catalog_admin", allEntries = true),
            @CacheEvict(value = "products_discount", allEntries = true)
    })
    @Transactional
    public ProductFullResponse patchProduct(ProductPatchRequest productPatchRequest, Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        productMapper.updateEntity(productPatchRequest, product);
        Optional.ofNullable(productPatchRequest.getProductCategory())
                .map(category -> ProductCategory.fromLabel(category))
                .ifPresent(category -> product.setProductCategory(category));
        if(productPatchRequest.getImages() != null){
            Map<Long, ProductImage> existingImages = product.getImages()
                    .stream()
                    .collect(Collectors.toMap(image -> image.getId(), image -> image));
            Set<Long> requestedIds = productPatchRequest.getImages()
                    .stream()
                    .map(image -> image.getId())
                    .filter(imageId -> imageId != null)
                    .collect(Collectors.toSet());
            if (!existingImages.keySet().containsAll(requestedIds)) {
                throw new BadRequestException("Some images do not belong to this product");
            }
            product.getImages().removeIf(image -> !requestedIds.contains(image.getId()));
            productPatchRequest.getImages().forEach(
                    image -> {
                        Optional.ofNullable(image.getId())
                                .map(imageId -> existingImages.get(imageId))
                                .ifPresentOrElse(
                                        updatedImage -> updatedImage.setUrl(image.getUrl()),
                                        () -> {
                                            ProductImage newImage = new ProductImage();
                                            newImage.setUrl(image.getUrl());
                                            newImage.setProduct(product);
                                            product.getImages().add(newImage);
                                        }
                                );
                    }
            );
        }
        if (productPatchRequest.getSpecifications() != null){
            Map<Long, TechnicalSpecification> existingTechnicalSpecifications = product.getSpecifications()
                    .stream()
                    .collect(Collectors.toMap(technicalSpecification -> technicalSpecification.getId(), technicalSpecification -> technicalSpecification));
            Set<Long> requestedIds = productPatchRequest.getSpecifications()
                    .stream()
                    .map(technicalSpecification -> technicalSpecification.getId())
                    .filter(technicalSpecificationId -> technicalSpecificationId != null)
                    .collect(Collectors.toSet());
            if (!existingTechnicalSpecifications.keySet().containsAll(requestedIds)) {
                throw new BadRequestException("Some specifications do not belong to this product");
            }
            product.getSpecifications().removeIf(technicalSpecification -> !requestedIds.contains(technicalSpecification.getId()));
            productPatchRequest.getSpecifications().forEach(technicalSpecification ->
                    Optional.ofNullable(technicalSpecification.getId())
                            .map(technicalSpecificationId -> existingTechnicalSpecifications.get(technicalSpecificationId))
                            .ifPresentOrElse(
                                    updatedTechnicalSpecification -> {
                                        updatedTechnicalSpecification.setName(technicalSpecification.getName());
                                        updatedTechnicalSpecification.setValue(technicalSpecification.getValue());
                                    },
                                    () -> {
                                        TechnicalSpecification newTechnicalSpecification = new TechnicalSpecification();
                                        newTechnicalSpecification.setName(technicalSpecification.getName());
                                        newTechnicalSpecification.setValue(technicalSpecification.getValue());
                                        newTechnicalSpecification.setProduct(product);
                                        product.getSpecifications().add(newTechnicalSpecification);
                                    }
                            )
            );
        }
        Product savedProduct = productRepository.save(product);
        ProductFullResponse productResponse = productMapper.toFullDto(savedProduct);
        return productResponse;
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

    @Cacheable(value = "product_item_admin", key = "#id")
    @Transactional(readOnly = true)
    public ProductFullResponse findByIdAll(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        List<Long> ids = List.of(product.getId());
        productRepository.findAllWithImagesByIdsIn(ids);
        productRepository.findAllWithSpecificationsByIdsIn(ids);
        ProductFullResponse productResponse = productMapper.toFullDto(product);
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
}
