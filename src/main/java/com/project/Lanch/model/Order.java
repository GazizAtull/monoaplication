package com.project.Lanch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Order represents a customer's order in the system.
 * - Contains information such as:
 *   - Unique ID.
 *   - Order date.
 *   - Associated user (customer) who placed the order.
 *   - List of OrderItems in the order.
 *   - Total amount for the order.
 * - Uses JPA annotations for ORM mapping to the "orders" table.
 */



@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private Double totalAmount;


}
