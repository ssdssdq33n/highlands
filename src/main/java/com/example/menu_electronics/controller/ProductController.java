package com.example.menu_electronics.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.menu_electronics.dto.ApiResponse;
import com.example.menu_electronics.dto.request.ProductRequest;
import com.example.menu_electronics.dto.response.ProductResponse;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.service.ProductService;
import com.example.menu_electronics.service.StorageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    ProductService productService;
    StorageService storageService;

    @PostMapping("/create")
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PostMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ProductResponse> uploadFile(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        System.out.println("file: " + file);
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.uploadImage(id, file))
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<ProductResponse> updateProduct(@RequestBody ProductRequest request, @PathVariable("id") Long id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(request, id))
                .build();
    }

//    @GetMapping("/convert")
//    ApiResponse<ProductResponse> convertImage(@RequestParam("imageUrl") String imageUrl, @RequestParam("id") Long id) throws Exception {
//        return ApiResponse.<ProductResponse>builder()
//                .result(productService.convertImageUrlToMultipartFile(imageUrl, id))
//                .build();
//    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<String> deleteProduct(@PathVariable("id") Long id) {
        return ApiResponse.<String>builder()
                .result(productService.deleteProduct(id))
                .build();
    }

    @GetMapping("/get/{id}")
    ApiResponse<ProductResponse> getProduct(@PathVariable("id") Long id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(id))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<ProductResponse>> getAllProduct() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/find")
    ApiResponse<List<ProductResponse>> findProduct(@RequestParam("keyword") String keyword) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findProductByKeyword(keyword))
                .build();
    }

    @GetMapping("/imagesUpload/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        Resource file = storageService.loadAsResource(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (Exception exception) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
