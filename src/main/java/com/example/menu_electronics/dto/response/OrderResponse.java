package com.example.menu_electronics.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    String userId;
    String name;
    String email;
    BigDecimal totalPrice;
    String description;
    String address;
    String phone;
    String orderDate;
    String orderDone;
    BigDecimal totalAmount;
    String status;
    String table_name;
    Boolean payment;
    String typePayment;
    List<OrderItemResponse> orderItemResponses;
}
