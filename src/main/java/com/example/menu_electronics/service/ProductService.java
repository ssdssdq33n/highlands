package com.example.menu_electronics.service;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.menu_electronics.dto.request.ProductInitRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.menu_electronics.dto.request.ProductRequest;
import com.example.menu_electronics.dto.response.ProductResponse;
import com.example.menu_electronics.entity.Category;
import com.example.menu_electronics.entity.Product;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.ProductMapper;
import com.example.menu_electronics.repository.CategoryRepository;
import com.example.menu_electronics.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    StorageService storageService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "products", allEntries = true) // Xóa cache khi thêm mới
    public ProductResponse createProduct(ProductRequest productRequest) {
        Optional<Product> productExisted = productRepository.findByName(productRequest.getName());
        if (productExisted.isPresent()) throw new AppException(ErrorCode.PRODUCT_EXISTED);
        Product product = productMapper.toProduct(productRequest);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Category category = categoryRepository
                .findById(productRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        product.setCategory(category);
        product.setCreated(orderTime.format(formatter));
        product.setUpdated(orderTime.format(formatter));
        product = productRepository.save(product);
        ProductResponse response = productMapper.toProductResponse(productRepository.save(product));
        response.setNameCategory(category.getName());
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ProductResponse uploadImage(Long id, MultipartFile file) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (!file.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();

            product.setImage(storageService.getStoredFilename(file, uuString));
            storageService.store(file, product.getImage());
        }
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public void saveProductInit(ProductInitRequest request, Category category) throws Exception {
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setRating(request.getRating());
        product.setDiscount(request.getDiscount());
        product.setAvailability(request.getAvailability());
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        product.setCreated(orderTime.format(formatter));
        product.setUpdated(orderTime.format(formatter));
        product = productRepository.save(product);
        convertImageUrlToMultipartFile(request.getImageUrl(), product.getId());
    }

    public void convertImageUrlToMultipartFile(String imageUrl, Long id) throws Exception {
        // Tải dữ liệu từ URL
        URL url = new URL(imageUrl);

//        // Tạo cấu hình proxy
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.60.117.103", 8085));
//
//        // Mở kết nối với proxy
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
//
//        connection.setConnectTimeout(10000);
//        connection.setReadTimeout(10000);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Kiểm tra mã phản hồi
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP response code: " + connection.getResponseCode());
        }

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Tạo MultipartFile từ byte array
            var file = new CustomMultipartFile(
                    "file",                      // Tên field
                    "image.jpg",                 // Tên file
                    connection.getContentType(), // MIME type
                    imageBytes                   // Nội dung
            );
            Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            if (!file.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();

                product.setImage(storageService.getStoredFilename(file, uuString));
                storageService.store(file, product.getImage());
            }
            productRepository.save(product);
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "products", allEntries = true) // Xóa cache khi thêm mới
    public ProductResponse updateProduct(ProductRequest productRequest, Long id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        //        Category category = categoryRepository
        //                .findById(productRequest.getCategoryId())
        //                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        product.setUpdated(orderTime.format(formatter));
        product.setName(productRequest.getName());
        product.setAvailability(productRequest.getAvailability());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setDiscount(productRequest.getDiscount());
        product.setRating(productRequest.getRating());
        //        product.setCategory(category);
        product = productRepository.save(product);
        ProductResponse response = productMapper.toProductResponse(productRepository.save(product));
        response.setNameCategory(product.getCategory().getName());
        return response;
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true) // Xóa cache khi thêm mới
    public String deleteProduct(Long id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productRepository.delete(product);
        return "Deleted Successfully";
    }

    //    @Cacheable(value = "allProducts")
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    ProductResponse response = productMapper.toProductResponse(product);
                    response.setNameCategory(product.getCategory().getName());
                    return response;
                })
                .toList();
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {
        Product product =
                productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        ProductResponse response = productMapper.toProductResponse(product);
        response.setNameCategory(product.getCategory().getName());
        return response;
    }

    public List<ProductResponse> findProductByKeyword(String keyword){
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return products.stream()
                .map(product -> {
                    ProductResponse response = productMapper.toProductResponse(product);
                    response.setNameCategory(product.getCategory().getName());
                    return response;
                })
                .toList();
    }
}
