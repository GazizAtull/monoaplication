package com.project.Lanch.factory;

import com.project.Lanch.model.Order;
import com.project.Lanch.model.OrderItem;
import com.project.Lanch.model.Product;


/**
 * OrderItemFactory is an interface for creating OrderItem objects.
 * - Defines a method to create an OrderItem using a Product, quantity, and associated Order.
 * - Provides a flexible way to implement different strategies for creating OrderItem instances.
 */



public interface OrderItemFactory {
    OrderItem createOrderItem(Product product, int quantity, Order order);
}
