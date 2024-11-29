package com.example.menu_electronics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.OrderItemRequest;
import com.example.menu_electronics.dto.response.OrderItemResponse;
import com.example.menu_electronics.service.OrderItemService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orderItem")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderItemController {
    OrderItemService orderItemService;

    @PostMapping("/createOrEdit")
    ApiResponse<OrderItemResponse> createOrderItem(@RequestBody OrderItemRequest orderItemRequest) {
        return ApiResponse.<OrderItemResponse>builder()
                .result(orderItemService.createOrderItem(orderItemRequest))
                .build();
    }

    @PostMapping("/update")
    ApiResponse<OrderItemResponse> updateOrderItem(@RequestBody OrderItemRequest orderItemRequest) {
        return ApiResponse.<OrderItemResponse>builder()
                .result(orderItemService.updateOrderItem(orderItemRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<String> deleteOrderItem(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(orderItemService.deleteOrderItem(id))
                .build();
    }

    @PostMapping("/deleteAll")
    ApiResponse<String> deleteAllOrderItem(@RequestBody List<Long> ids) {
        return ApiResponse.<String>builder()
                .result(orderItemService.deleteAllOrderItem(ids))
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<OrderItemResponse> getOrderItem(@PathVariable("id") Long id) {
        return ApiResponse.<OrderItemResponse>builder()
                .result(orderItemService.getOrderItemById(id))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<OrderItemResponse>> getAllOrderItem() {
        return ApiResponse.<List<OrderItemResponse>>builder()
                .result(orderItemService.getAllOrderItems())
                .build();
    }
}
