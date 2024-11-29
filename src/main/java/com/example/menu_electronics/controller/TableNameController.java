package com.example.menu_electronics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.TableNameRequest;
import com.example.menu_electronics.dto.response.TableNameResponse;
import com.example.menu_electronics.service.TableNameService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TableNameController {
    TableNameService tableNameService;

    @PostMapping("/create")
    ApiResponse<TableNameResponse> createTable(@RequestBody TableNameRequest request) {
        return ApiResponse.<TableNameResponse>builder()
                .result(tableNameService.createTableName(request))
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<TableNameResponse> createTable(@RequestBody TableNameRequest request, @PathVariable("id") Long id) {
        return ApiResponse.<TableNameResponse>builder()
                .result(tableNameService.updateTableName(request, id))
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<TableNameResponse> getTable(@PathVariable("id") Long id) {
        return ApiResponse.<TableNameResponse>builder()
                .result(tableNameService.getTableName(id))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<TableNameResponse>> getAllTable() {
        return ApiResponse.<List<TableNameResponse>>builder()
                .result(tableNameService.getAllTableName())
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<String> createTable(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(tableNameService.deleteTableName(id))
                .build();
    }
}
