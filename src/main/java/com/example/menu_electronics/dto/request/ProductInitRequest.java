package com.example.menu_electronics.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInitRequest {

    String name;
    String description;
    BigDecimal price;
    String imageUrl;
    Boolean availability;
    Integer discount;
    Double rating;
    String nameCategory;
}
