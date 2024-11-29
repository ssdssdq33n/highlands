package com.example.menu_electronics.dto.request;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AskCustomerRequest {
    String table_name;
    String description;
    List<Long> selectedIds;
}
