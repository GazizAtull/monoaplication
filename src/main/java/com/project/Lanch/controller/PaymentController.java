package com.project.Lanch.controller;

import com.project.Lanch.model.CartItem;
import com.project.Lanch.model.Order;
import com.project.Lanch.model.User;
import com.project.Lanch.service.CartService;

import com.project.Lanch.service.OrderService;
import com.project.Lanch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import java.util.List;


/**
 * PaymentController manages the payment process for authenticated users.
 * - Provides endpoints for:
 *   - Displaying the payment page with cart details and the total price.
 *   - Processing payments and validating user input.
 * - Handles cart clearing and order creation upon successful payment.
 * - Sends notifications for orders and gracefully handles email-related exceptions.
 * - Ensures only authenticated users can access the payment functionality.
 */




@Controller
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;



    @GetMapping("/payment")
    public String showPaymentPage(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(userDetails.getUsername());
        List<CartItem> cart = cartService.getCart(session);

        if (cart.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "payment";
        }

        double total = cartService.getTotalPrice(cart);

        model.addAttribute("user", user);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "payment";
    }

    @PostMapping("/payment")
    public String processPayment(@RequestParam("cardNumber") String cardNumber,
                                 @RequestParam("expiryDate") String expiryDate,
                                 @RequestParam("cardHolder") String cardHolder,
                                 @RequestParam("cvc") String cvc,
                                 Model model,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 HttpSession session) {

        if (userDetails == null) {
            return "redirect:/login";

        }

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cardHolder.isEmpty() || cvc.isEmpty()) {
            model.addAttribute("error", "Please fill out all fields.");
            return "payment";
        }

        User user = userService.findByUsername(userDetails.getUsername());
        List<CartItem> cart = cartService.getCart(session);


        if (cart.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "payment";
        }

        try {
            Order order = orderService.createOrder(user, cart);

            cartService.clearCart(cart);


            model.addAttribute("order", order);
            return "success";
        } catch (MessagingException | MailException e) {
            model.addAttribute("error", "Error sending email.");
            return "payment";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while processing your order.");
            return "payment";
        }
    }

}
