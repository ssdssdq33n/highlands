package com.example.menu_electronics.mapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.TableNameRequest;
import com.example.menu_electronics.dto.response.TableNameResponse;
import com.example.menu_electronics.entity.TableName;

@Mapper(componentModel = "spring")
public interface TableNameMapper {
    TableName toTableName(TableNameRequest tableNameRequest);

    TableNameResponse toTableNameResponse(TableName tableName);
}
