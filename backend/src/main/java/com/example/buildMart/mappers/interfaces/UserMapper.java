package com.example.buildMart.mappers.interfaces;

import com.example.buildMart.dtos.requests.UserAuthRequest;
import com.example.buildMart.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserAuthRequest userAuthRequest);
}
