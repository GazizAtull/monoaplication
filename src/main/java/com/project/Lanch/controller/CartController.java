package com.project.Lanch.controller;

import com.project.Lanch.model.CartItem;
import com.project.Lanch.service.CartService;
import com.project.Lanch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;



/**
 * CartController manages shopping cart operations for authenticated users.
 * - Provides endpoints for:
 *   - Viewing the cart and its total price/quantity.
 *   - Adding products to the cart.
 *   - Removing individual products from the cart.
 *   - Updating product quantities in the cart.
 *   - Clearing the entire cart.
 * - Ensures that only authenticated users can access these features.
 * - Uses the CartService for managing cart operations stored in the user's session.
 */




@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String showCart(Model model,
                           @AuthenticationPrincipal UserDetails userDetails,
                           HttpSession session) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<CartItem> cart = cartService.getCart(session);
        double totalPrice = cartService.getTotalPrice(cart);
        int totalQuantity = cartService.getTotalQuantity(cart);

        model.addAttribute("cartItems", cart);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalQuantity", totalQuantity);

        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCartPath(@PathVariable("productId") Long productId,
                                @AuthenticationPrincipal UserDetails userDetails,
                                HttpSession session) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        cartService.addToCart(cartService.getCart(session), productId);

        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 HttpSession session) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        cartService.removeFromCart(cartService.getCart(session), productId);

        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 HttpSession session) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<CartItem> cart = cartService.getCart(session);

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    cartService.removeFromCart(cart, productId);
                }
                break;
            }
        }

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails,
                            HttpSession session) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        cartService.clearCart(cartService.getCart(session));

        return "redirect:/cart";
    }
}
