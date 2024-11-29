package com.example.menu_electronics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.FeedBackRequest;
import com.example.menu_electronics.dto.response.FeedBackResponse;
import com.example.menu_electronics.service.FeedBackService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FeedBackController {

    FeedBackService feedBackService;

    @PostMapping("/create")
    ApiResponse<FeedBackResponse> createFeedBack(@RequestBody FeedBackRequest request) {
        return ApiResponse.<FeedBackResponse>builder()
                .result(feedBackService.createFeedBack(request))
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<FeedBackResponse> getFeedBack(@PathVariable("id") Long id) {
        return ApiResponse.<FeedBackResponse>builder()
                .result(feedBackService.getFeedBack(id))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<FeedBackResponse>> getAllFeedBack() {
        return ApiResponse.<List<FeedBackResponse>>builder()
                .result(feedBackService.getAllFeedBack())
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<String> deleteFeedBack(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(feedBackService.deleteFeedBack(id))
                .build();
    }
}
