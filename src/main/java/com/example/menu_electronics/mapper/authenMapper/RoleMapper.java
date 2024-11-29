package com.example.menu_electronics.mapper.authenMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.menu_electronics.dto.request.authenRequest.RoleRequest;
import com.example.menu_electronics.dto.response.authenResponse.RoleResponse;
import com.example.menu_electronics.entity.authen.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
