package com.example.menu_electronics.mapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.CategoryRequest;
import com.example.menu_electronics.dto.response.CategoryResponse;
import com.example.menu_electronics.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);
}
