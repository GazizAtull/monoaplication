package com.project.Lanch.controller;

import com.project.Lanch.model.CartItem;
import com.project.Lanch.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import java.util.List;


/**
 * GlobalControllerAdvice provides global attributes for all controllers.
 * - Defines a `@ModelAttribute` named "totalQuantity" to calculate and make the total
 *   quantity of items in the cart available to all views.
 * - Uses the CartService to compute the total quantity from the cart stored in the user's session.
 */



@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartService cartService;


    @ModelAttribute("totalQuantity")
    public int getTotalQuantity(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            return 0;
        }
        return cartService.getTotalQuantity(cart);
    }
}
