package com.example.menu_electronics.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {

    Long id;

    String email;

    String description;

    BigDecimal totalPrice;

    String name;

    String address;

    String phone;

    Boolean payment;

    String typePayment;
}
