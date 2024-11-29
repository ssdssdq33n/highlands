package com.example.menu_electronics.mapper.authenMapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.authenRequest.PermissionRequest;
import com.example.menu_electronics.dto.response.authenResponse.PermissionResponse;
import com.example.menu_electronics.entity.authen.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
