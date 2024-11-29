package com.example.menu_electronics.dto.response;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AskCustomerResponse {
    Long id;
    String table_name;
    String description;
    List<String> selectionNames;
}
