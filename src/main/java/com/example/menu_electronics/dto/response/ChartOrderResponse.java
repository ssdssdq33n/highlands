package com.example.menu_electronics.dto.response;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChartOrderResponse {
    BigDecimal totalPrice;
    String date;
    Long totalOrderItems;
    String traceId;
}
