package com.example.menu_electronics.entity;

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
public class NotifySystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String table_name;
    String content;

    String created_at;

    Long notificationId;
    Long orderId;

    Boolean readNotify;
}
