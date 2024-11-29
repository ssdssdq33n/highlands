package com.example.menu_electronics.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {
    @NotBlank(message = "ProductId cannot be blank")
    Long productId;

    @NotBlank(message = "Quantity cannot be blank")
    Integer quantity;

    @NotBlank(message = "OrderId cannot be blank")
    Long orderId;

    String description;

    String size;
}
