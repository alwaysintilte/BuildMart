package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.ProductResponse;
import com.example.buildMart.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TechnicalSpecificationMapper.class})
public interface ProductMapper {
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
    ProductResponse toDto(Product product);

    List<ProductResponse> toDtoList(List<Product> productList);
}
