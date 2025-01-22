package com.project.Lanch.service;

import com.project.Lanch.factory.OrderItemFactory;
import com.project.Lanch.model.*;
import com.project.Lanch.repository.OrderItemRepository;
import com.project.Lanch.repository.OrderRepository;
import com.project.Lanch.repository.ProductRepository;
import com.project.Lanch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * OrderService manages the creation and handling of orders.
 * - Functions:
 *   - `createOrder`: Creates an order for a user based on the items in their cart.
 *     - Retrieves product details for each cart item.
 *     - Uses an OrderItemFactory to create OrderItem instances.
 *     - Calculates the total amount for the order.
 *     - Saves the order and its items to the database.
 *     - Sends an order confirmation email via EmailService.
 * - Dependencies:
 *   - OrderRepository for saving orders.
 *   - ProductRepository for retrieving product details.
 *   - UserRepository for accessing user data.
 *   - OrderItemFactory for creating order items.
 *   - EmailService for sending notifications.
 * - Uses transactional handling to ensure the order creation process is atomic.
 * - Throws `MessagingException` for email-related errors.
 */



@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemFactory orderItemFactory;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(User user, List<CartItem> cartItems) throws MessagingException {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            OrderItem orderItem = orderItemFactory.createOrderItem(product, cartItem.getQuantity(), order);
            total += product.getPrice() * cartItem.getQuantity();
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        emailService.sendOrderConfirmationEmail(savedOrder);

        return savedOrder;
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public List<Object[]> getPopularItems() {

        return orderItemRepository.findPopularItemsWithProduct();
    }
    public List<Object[]> getPopularItemsforAdmin() {

        return orderItemRepository.findPopularItems();
    }

}
