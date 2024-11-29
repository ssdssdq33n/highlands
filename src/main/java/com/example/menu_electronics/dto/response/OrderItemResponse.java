package com.example.menu_electronics.dto.response;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    Long id;
    Long productId;
    String nameProduct;
    Integer quantity;
    BigDecimal price;
    Long orderId;
    String description;
    String imageUrl;
    String size;
}
