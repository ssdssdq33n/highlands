package com.example.menu_electronics.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.menu_electronics.dto.request.CategoryRequest;
import com.example.menu_electronics.dto.response.CategoryResponse;
import com.example.menu_electronics.dto.response.ProductResponse;
import com.example.menu_electronics.entity.Category;
import com.example.menu_electronics.entity.Product;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.CategoryMapper;
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
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "categories", allEntries = true) // Xóa cache khi thêm mới
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Optional<Category> categoryExisted = categoryRepository.findByName(categoryRequest.getName());
        if (categoryExisted.isPresent()) throw new AppException(ErrorCode.CATEGORY_EXISTED);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Category category = categoryMapper.toCategory(categoryRequest);
        category.setCreated(orderTime.format(formatter));
        category.setUpdated(orderTime.format(formatter));
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "categories", allEntries = true) // Xóa cache khi cập nhật
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setUpdated(orderTime.format(formatter));
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional
    @CacheEvict(value = "categories", allEntries = true) // Xóa cache khi xóa
    public String deleteCategory(Long id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryRepository.delete(category);
        return "Deleted Successfully";
    }

    @Cacheable(value = "categories", key = "#id") // Cache kết quả của phương thức getCategory
    public CategoryResponse getCategory(Long id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
        List<ProductResponse> productResponseList = new ArrayList<>();
        category.getProducts().stream()
                .map(product -> {
                    ProductResponse productResponse = productMapper.toProductResponse(product);
                    productResponse.setNameCategory(category.getName());
                    return productResponse;
                })
                .forEach(productResponseList::add);
        categoryResponse.setProductResponses(productResponseList);
        return categoryResponse;
    }

    //    @Cacheable(value = "allCategories") // Cache toàn bộ danh sách category
    public List<CategoryResponse> getAllCategories() {
        List<Category> category = categoryRepository.findAll();
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        category.stream()
                .map(item -> {
                    CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(item);
                    List<ProductResponse> productResponseList = new ArrayList<>();
                    item.getProducts().stream()
                            .map(product -> {
                                ProductResponse productResponse = productMapper.toProductResponse(product);
                                productResponse.setNameCategory(item.getName());
                                return productResponse;
                            })
                            .forEach(productResponseList::add);
                    categoryResponse.setProductResponses(productResponseList);
                    return categoryResponse;
                })
                .forEach(categoryResponseList::add);
        return categoryResponseList;
    }

    //    @Cacheable(value = "categories_search", key = "#keyword")
    public List<CategoryResponse> searchCategoriesWithProducts(String keyword) {
        // Tìm các Category có tên khớp với từ khóa tìm kiếm
        List<Category> categoriesByName = categoryRepository.findByNameContainingIgnoreCase(keyword);

        // Tìm các Product có tên khớp với từ khóa tìm kiếm
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);

        // Lấy các Category liên quan đến sản phẩm tìm được
        List<Category> categoriesByProduct = products.stream()
                .map(Product::getCategory)
                .distinct() // Đảm bảo không có Category trùng lặp
                .collect(Collectors.toList());

        // Gộp các Category từ cả tên và sản phẩm
        Set<Category> allCategories = new HashSet<>(categoriesByName);
        allCategories.addAll(categoriesByProduct);

        // Tạo map để nhóm sản phẩm theo categoryId
        Map<Long, List<Product>> categoryProductMap = products.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().getId()));

        // Map các category và sản phẩm tương ứng vào CategoryResponse
        return allCategories.stream()
                .map(category -> {
                    CategoryResponse response = categoryMapper.toCategoryResponse(category);

                    // Lấy danh sách sản phẩm cho category này (nếu có)
                    List<Product> categoryProducts =
                            categoryProductMap.getOrDefault(category.getId(), new ArrayList<>());

                    // Map danh sách sản phẩm sang ProductResponse
                    List<ProductResponse> productResponses = categoryProducts.stream()
                            .map(product -> {
                                ProductResponse productResponse = productMapper.toProductResponse(product);
                                productResponse.setNameCategory(category.getName());
                                return productResponse;
                            })
                            .collect(Collectors.toList());

                    response.setProductResponses(productResponses);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
