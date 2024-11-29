package com.example.menu_electronics.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableNameResponse {
    Long id;
    String name;
    String created;
    String updated;
}
