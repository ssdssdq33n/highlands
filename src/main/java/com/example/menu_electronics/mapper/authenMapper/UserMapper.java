package com.example.menu_electronics.mapper.authenMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.menu_electronics.dto.request.authenRequest.UserCreationRequest;
import com.example.menu_electronics.dto.request.authenRequest.UserUpdateRequest;
import com.example.menu_electronics.dto.response.authenResponse.UserResponse;
import com.example.menu_electronics.entity.authen.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
