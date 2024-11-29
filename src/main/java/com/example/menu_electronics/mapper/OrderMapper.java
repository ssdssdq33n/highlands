package com.example.menu_electronics.mapper;

import org.mapstruct.Mapper;

import com.example.menu_electronics.dto.request.OrderRequest;
import com.example.menu_electronics.dto.response.OrderResponse;
import com.example.menu_electronics.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    Order toOrder(OrderRequest orderRequest);
}
