package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.responses.TechnicalSpecificationResponse;
import com.example.buildMart.models.TechnicalSpecification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnicalSpecificationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", source = "value")
    TechnicalSpecification toEntity(TechnicalSpecificationResponse technicalSpecificationResponse);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", source = "value")
    TechnicalSpecificationResponse toDto(TechnicalSpecification technicalSpecification);

    List<TechnicalSpecificationResponse> toDtoList(List<TechnicalSpecification> technicalSpecificationList);
}
