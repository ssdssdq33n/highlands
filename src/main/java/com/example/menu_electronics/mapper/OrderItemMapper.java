package com.example.menu_electronics.mapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.OrderItemRequest;
import com.example.menu_electronics.dto.response.OrderItemResponse;
import com.example.menu_electronics.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    OrderItem toOrderItem(OrderItemRequest orderItemRequest);
}
