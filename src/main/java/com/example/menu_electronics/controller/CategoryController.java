package com.example.menu_electronics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.CategoryRequest;
import com.example.menu_electronics.dto.response.CategoryResponse;
import com.example.menu_electronics.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/create")
    ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<CategoryResponse> updateCategory(@RequestBody CategoryRequest request, @PathVariable("id") Long id) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(request, id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<String> deleteCategory(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(categoryService.deleteCategory(id))
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<CategoryResponse> getCategory(@PathVariable("id") Long id) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getCategory(id))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<CategoryResponse>> getAllCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<CategoryResponse>> searchCategoriesWithProducts(@RequestParam String keyword) {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.searchCategoriesWithProducts(keyword))
                .build();
    }
}
