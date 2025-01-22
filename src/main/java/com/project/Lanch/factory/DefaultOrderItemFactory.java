package com.project.Lanch.factory;

import com.project.Lanch.model.Order;
import com.project.Lanch.model.OrderItem;
import com.project.Lanch.model.Product;

import org.springframework.stereotype.Component;

/**
 * DefaultOrderItemFactory is a concrete implementation of the OrderItemFactory interface.
 * - Responsible for creating OrderItem objects.
 * - Associates the created OrderItem with a specific Product, quantity, and Order.
 * - Sets the price of the OrderItem based on the Product's price.
 */



@Component
public class DefaultOrderItemFactory implements OrderItemFactory {

    @Override
    public OrderItem createOrderItem(Product product, int quantity, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        return orderItem;
    }
}