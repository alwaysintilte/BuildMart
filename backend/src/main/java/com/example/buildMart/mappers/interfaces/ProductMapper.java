package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.requests.ProductPatchRequest;
import com.example.buildMart.dtos.requests.ProductPostRequest;
import com.example.buildMart.dtos.responses.ProductFullResponse;
import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.models.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TechnicalSpecificationMapper.class, ProductImageMapper.class})
public interface ProductMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "productCategory", expression = "java(product.getProductCategory().getLabel())")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "images", expression = "java(product.getImages().stream().map(com.example.buildMart.models.ProductImage::getUrl).toList())")
    @Mapping(target = "specifications", source = "specifications")
    ProductResponse toDto(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "productCategory", expression = "java(product.getProductCategory().getLabel())")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "specifications", source = "specifications")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    ProductFullResponse toFullDto(Product product);

    List<ProductResponse> toDtoList(List<Product> productList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "price", source = "price")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "specifications", ignore = true)
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Product toEntity(ProductPostRequest productPostRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "price", source = "price")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "specifications", ignore = true)
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(ProductPatchRequest productPatchRequest, @MappingTarget Product product);
}
