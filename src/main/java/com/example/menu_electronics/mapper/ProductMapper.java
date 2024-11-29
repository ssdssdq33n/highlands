package com.example.menu_electronics.mapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.ProductRequest;
import com.example.menu_electronics.dto.response.ProductResponse;
import com.example.menu_electronics.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest request);

    ProductResponse toProductResponse(Product product);
}
