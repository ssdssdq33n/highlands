package com.example.menu_electronics.entity.authen;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String username;
    String password;
    String email;
    String created;
    String updated;

    @ManyToMany
    Set<Role> roles;

    String status;
    String gender;

    Boolean isCustomer;
}
