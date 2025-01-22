package com.project.Lanch.decorator;


/**
 * ProductComponent is a base interface for products in the decorator pattern.
 * - Defines methods for retrieving a product's price, description, and name.
 * - Enables flexibility for extending product functionality using decorators.
 */


public interface ProductComponent {
    Double getPrice();
    String getDescription();
    String getName();
}