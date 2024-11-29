package com.example.menu_electronics.dto.response;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String name;
    String description;
    String image;
    BigDecimal price;
    Boolean availability;
    Integer discount;
    Double rating;
    String created;
    String updated;
    String nameCategory;
}
