package com.project.Lanch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * OrderItem represents an individual item in an Order.
 * - Contains details such as:
 *   - Unique ID.
 *   - Associated Order.
 *   - Product being purchased.
 *   - Quantity of the product.
 *   - Price of the product at the time of the order.
 * - Uses JPA annotations for ORM mapping to the "order_items" table.
 */



@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    private Product product;

    private int quantity;

    private Double price;

    public OrderItem(Order order, Product product, int quantity, Double price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
