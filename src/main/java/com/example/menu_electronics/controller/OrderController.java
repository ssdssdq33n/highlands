package com.example.menu_electronics.controller;

import java.util.List;

import com.example.menu_electronics.dto.request.OrderUpdateRequest;
import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.OrderRequest;
import com.example.menu_electronics.dto.response.ChartDashboard;
import com.example.menu_electronics.dto.response.ChartOrderResponse;
import com.example.menu_electronics.dto.response.OrderResponse;
import com.example.menu_electronics.service.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
    OrderService orderService;

    @GetMapping("/create")
    ApiResponse<Long> createOrder(@RequestParam("userId") String userId) {
        return ApiResponse.<Long>builder()
                .result(orderService.createOrder(userId))
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<OrderResponse> updateOrder(@RequestBody OrderRequest orderRequest, @PathVariable("id") Long id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrder(orderRequest, id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<String> deleteOrder(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(orderService.deleteOrder(id))
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<OrderResponse> getOrder(@PathVariable("id") Long id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrder(id))
                .build();
    }

    @GetMapping("/getProcessing/{id}")
    ApiResponse<OrderResponse> getOrderProcessing(@PathVariable("id") Long id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderProcessing(id))
                .build();
    }

    @GetMapping("/getById/{id}")
    ApiResponse<OrderResponse> getOrderById(@PathVariable("id") Long id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderId(id))
                .build();
    }

    @GetMapping("/send/{id}")
    ApiResponse<String> sendOrder(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder().result(orderService.sendOrder(id)).build();
    }

    @PostMapping("/setPayment/{id}")
    ApiResponse<String> setPaymentOrder(@PathVariable("id") Long id, @RequestBody String type) {
        return ApiResponse.<String>builder()
                .result(orderService.setTypePaymentOrder(type, id))
                .build();
    }

    @GetMapping("/done/{id}")
    ApiResponse<String> doneOrder(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder().result(orderService.doneOrder(id)).build();
    }

    @PostMapping("/ship")
    ApiResponse<String> shipOrder(@RequestBody OrderUpdateRequest request) {
        return ApiResponse.<String>builder().result(orderService.shipOrder(request)).build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<OrderResponse>> getAllOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    @GetMapping("/last7days/totalsales")
    ApiResponse<List<ChartOrderResponse>> getDailyTotalSalesForLast7Days(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        return ApiResponse.<List<ChartOrderResponse>>builder()
                .result(orderService.getDailyTotalSalesForLast7Days(startTime, endTime))
                .build();
    }

    @GetMapping("/dashboard")
    ApiResponse<ChartDashboard> getAllDashboard() {
        return ApiResponse.<ChartDashboard>builder()
                .result(orderService.getTotalDashboard())
                .build();
    }
}
