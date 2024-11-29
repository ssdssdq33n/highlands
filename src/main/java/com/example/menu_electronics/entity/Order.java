package com.example.menu_electronics.entity;

import java.math.BigDecimal;
import java.util.List;

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
@Table(name = "`order`") // Sử dụng backticks cho từ khóa
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String table_name;

    String userId;

    String name;

    String email;

    String description;

    String address;

    String phone;

    @Column(name = "order_date")
    String orderDate;

    @Column(name = "order_done")
    String orderDone;

    BigDecimal totalAmount;

    BigDecimal totalPrice;

    String status;

    Boolean payment;

    String typePayment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItems;
}
