package com.project.Lanch.service;

import com.project.Lanch.model.CartItem;
import com.project.Lanch.model.Product;
import com.project.Lanch.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/**
 * CartService manages shopping cart operations.
 * - Functions:
 *   - `addToCart`: Adds a product to the cart or increments the quantity if it already exists.
 *   - `removeFromCart`: Removes a product from the cart.
 *   - `clearCart`: Clears all items in the cart.
 *   - `getTotalQuantity`: Calculates the total quantity of items in the cart.
 *   - `getTotalPrice`: Calculates the total price of all items in the cart.
 *   - `getCart`: Retrieves the cart from the session, initializing it if it doesn't exist.
 * - Interacts with the ProductRepository to fetch product details.
 * - Ensures session management for the cart across user interactions.
 */



@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(List<CartItem> cart, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return;
        }

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.incrementQuantity();
                return;
            }
        }
        cart.add(new CartItem(product));
    }

    public void removeFromCart(List<CartItem> cart, Long productId) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart(List<CartItem> cart) {
        cart.clear();
    }

    public int getTotalQuantity(List<CartItem> cart) {
        return cart.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getTotalPrice(List<CartItem> cart) {
        return cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
