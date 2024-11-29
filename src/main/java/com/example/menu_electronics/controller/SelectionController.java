package com.example.menu_electronics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.SelectionRequest;
import com.example.menu_electronics.dto.response.SelectionResponse;
import com.example.menu_electronics.service.SelectionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/selection")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SelectionController {

    SelectionService selectionService;

    @PostMapping("/create")
    ApiResponse<SelectionResponse> createSelection(@RequestBody SelectionRequest request) {
        return ApiResponse.<SelectionResponse>builder()
                .result(selectionService.createSelection(request))
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<SelectionResponse> updateSelection(@RequestBody SelectionRequest request, @PathVariable("id") Long id) {
        return ApiResponse.<SelectionResponse>builder()
                .result(selectionService.updateSelection(request, id))
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<SelectionResponse> getSelection(@PathVariable("id") Long id) {
        return ApiResponse.<SelectionResponse>builder()
                .result(selectionService.getSelection(id))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<SelectionResponse>> getAllSelection() {
        return ApiResponse.<List<SelectionResponse>>builder()
                .result(selectionService.getAllSelections())
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<String> deleteSelection(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(selectionService.deleteSelection(id))
                .build();
    }
}
