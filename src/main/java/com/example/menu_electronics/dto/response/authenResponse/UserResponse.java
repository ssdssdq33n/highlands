package com.example.menu_electronics.dto.response.authenResponse;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String name;
    String username;
    String email;
    String created;
    String updated;
    Set<RoleResponse> roles;
    String status;
    String gender;
    Boolean isCustomer;
}
