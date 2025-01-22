package com.project.Lanch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Product represents a product available for purchase.
 * - Fields include:
 *   - Unique ID.
 *   - Name and description (cannot be empty).
 *   - Price (must be positive).
 *   - Quantity in stock (cannot be negative).
 *   - Optional fields: formatted prices, image URL, sale status, rating, and original price.
 * - Uses JPA annotations for ORM mapping to the "products" table.
 * - Includes validation constraints to ensure proper input for name, description, price, and quantity.
 */





@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Price cannot be empty")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    private String formattedPrice;

    private String formattedOriginalPrice;

    private String imageUrl;

    private boolean sale;

    private Integer rating;

    private Double originalPrice;
}
