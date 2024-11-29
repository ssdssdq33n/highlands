package com.example.menu_electronics.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "Name cannot be blank")
    String name;

    String description;
    MultipartFile file;

    @NotBlank(message = "Price cannot be blank")
    BigDecimal price;

    @NotBlank(message = "Availability cannot be blank")
    Boolean availability;

    @NotBlank(message = "discount cannot be blank")
    Integer discount;

    @NotBlank(message = "Rating cannot be blank")
    Double rating;

    @NotBlank(message = "CategoryId cannot be blank")
    Long categoryId;
}
