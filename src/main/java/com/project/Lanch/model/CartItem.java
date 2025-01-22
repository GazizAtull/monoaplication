package com.project.Lanch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * CartItem represents an item in the shopping cart.
 * - Contains a reference to the Product and its quantity.
 * - Provides methods to increment and decrement the quantity.
 * - Calculates the total price based on the product's price and quantity.
 */




@Getter
@Setter
@NoArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    public double getTotalPrice() {
        return this.product.getPrice() * this.quantity;
    }
}
