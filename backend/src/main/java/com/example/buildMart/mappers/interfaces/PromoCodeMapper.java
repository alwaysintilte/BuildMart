package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.PromoCodeResponse;
import com.example.buildMart.models.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    @Mapping(target = "code", source = "code")
    @Mapping(target = "discountPercentage", source = "discountPercentage")
    PromoCodeResponse toDto(PromoCode promoCode);
}
