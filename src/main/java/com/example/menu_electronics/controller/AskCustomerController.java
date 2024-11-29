package com.example.menu_electronics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.AskCustomerRequest;
import com.example.menu_electronics.dto.response.AskCustomerResponse;
import com.example.menu_electronics.service.AskCustomerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ask")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AskCustomerController {

    AskCustomerService notificationService;

    @PostMapping("/create")
    ApiResponse<String> createAsk(@RequestBody AskCustomerRequest request) {
        return ApiResponse.<String>builder()
                .result(notificationService.creatAsk(request))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<AskCustomerResponse>> getAllAsk() {
        return ApiResponse.<List<AskCustomerResponse>>builder()
                .result(notificationService.getAllAsks())
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<AskCustomerResponse> getAsk(@PathVariable("id") Long id) {
        return ApiResponse.<AskCustomerResponse>builder()
                .result(notificationService.getAskById(id))
                .build();
    }

    @PostMapping("/delete/{id}")
    ApiResponse<String> deleteAsk(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(notificationService.deleteAskById(id))
                .build();
    }
}
