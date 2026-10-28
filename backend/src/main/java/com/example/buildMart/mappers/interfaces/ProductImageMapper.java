package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.ProductImageResponse;
import com.example.buildMart.models.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "url", source = "url")
    ProductImageResponse toDto(ProductImage productImage);
}
