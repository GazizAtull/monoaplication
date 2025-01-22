package com.project.Lanch.controller;

import com.project.Lanch.model.Product;
import com.project.Lanch.model.UserProfile;
import com.project.Lanch.service.CartService;
import com.project.Lanch.service.OrderService;
import com.project.Lanch.service.ProductService;
import com.project.Lanch.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


/**
 * HomeController handles the core functionality of the application, including:
 * - Displaying the home page with a list of products, formatted for presentation.
 * - Providing details for individual products.
 * - Displaying the "About" page.
 * - Handling user profile viewing and editing.
 * - Ensuring authenticated users can access and update their profile information.
 * - Logging debug and warning messages for profile-related actions.
 */



@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;


    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal().equals("anonymousUser"))) {
            String username = authentication.getName();
            model.addAttribute("username", username);
        }

        List<Product> products = productService.findAllProducts();

        for (Product product : products) {
            product.setFormattedPrice(formatPrice(BigDecimal.valueOf(product.getPrice())));
            if (product.getOriginalPrice() != null) {
                product.setFormattedOriginalPrice(formatPrice(BigDecimal.valueOf(product.getOriginalPrice())));
            }
        }

        model.addAttribute("products", products);

        return "index";
    }

    private String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP).toString();
    }

    @GetMapping("/shop/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Product product = productService.findProductById(id);
        if (product == null) {
            return "redirect:/?error=ProductNotFound";
        }
        model.addAttribute("product", product);
        return "product-details";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            com.project.Lanch.model.User user = userService.findByUsername(username);
            com.project.Lanch.model.UserProfile userProfile = userService.findUserProfileByUsername(username);

            if (user != null && userProfile != null) {
                model.addAttribute("user", user);
                model.addAttribute("userProfile", userProfile);
            } else {
                model.addAttribute("errorMessage", "User profile not found. Please update your profile information.");
            }
        } else {
            model.addAttribute("errorMessage", "You must log in to view the profile.");
        }
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            com.project.Lanch.model.User user = userService.findByUsername(username);
            com.project.Lanch.model.UserProfile userProfile = userService.findUserProfileByUsername(username);

            if (user != null && userProfile != null) {
                model.addAttribute("userProfile", userProfile);
            } else {
                model.addAttribute("errorMessage", "User profile not found.");
                return "redirect:/profile";
            }
        } else {
            model.addAttribute("errorMessage", "You must log in to edit the profile.");
            return "redirect:/profile";
        }
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("userProfile") UserProfile userProfile, BindingResult result, Model model) {
        logger.debug("Received profile edit request: ID={}, Email={}", userProfile.getId(), userProfile.getEmail());

        if (result.hasErrors()) {
            logger.debug("Validation errors found in profile: {}", result.getAllErrors());
            model.addAttribute("errorMessage", "Form contains errors. Please check the entered data.");
            return "profile-edit";
        }

        UserProfile existingProfile = userService.findUserProfileById(userProfile.getId());
        if (existingProfile != null) {
            logger.debug("Updating existing user profile with ID={}", userProfile.getId());
            existingProfile.setEmail(userProfile.getEmail());
            existingProfile.setFullName(userProfile.getFullName());
            existingProfile.setPhone(userProfile.getPhone());
            existingProfile.setAddress(userProfile.getAddress());
            userService.saveUserProfile(existingProfile);
        } else {
            logger.warn("User profile with ID={} not found.", userProfile.getId());
            model.addAttribute("errorMessage", "Profile not found. Please try again.");
            return "profile-edit";
        }

        logger.debug("Profile successfully updated for user ID={}", userProfile.getId());
        return "redirect:/profile";
    }


    @GetMapping("/shop/popularItem")
    public String popularItems(Model model) {
        List<Object[]> popularItems = orderService.getPopularItems();

        model.addAttribute("popularItems", popularItems);
        return "popular-items";
    }

}
