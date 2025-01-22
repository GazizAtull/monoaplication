package com.project.Lanch.controller;

import com.project.Lanch.command.SaveProductCommand;
import com.project.Lanch.command.SaveUserCommand;
import com.project.Lanch.decorator.ProductComponent;
import com.project.Lanch.model.Order;
import com.project.Lanch.model.Product;
import com.project.Lanch.model.User;
import com.project.Lanch.observer.EmailNotificationObserver;
import com.project.Lanch.service.EmailService;
import com.project.Lanch.service.OrderService;
import com.project.Lanch.service.ProductService;
import com.project.Lanch.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/**
 * AdminController handles all administrative operations for managing users and products.
 * It provides functionalities such as:
 * - Viewing and managing users and products.
 * - Adding, editing, and deleting users and products.
 * - Applying discounts to products.
 * - Searching for products.
 * - Observing changes and notifying via email when a product is saved.
 * All methods are secured and require the "ADMIN" role for access.
 */



@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderService orderService;


    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.findAllUsers();
        List<Product> products = productService.findAllProducts();
        model.addAttribute("users", users);
        model.addAttribute("products", products);
        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin-users";
    }

    @GetMapping("/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-add-user";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            new SaveUserCommand(user, userService, passwordEncoder).execute();
            redirectAttributes.addFlashAttribute("successMessage", "User saved successfully.");
        } catch (IllegalArgumentException e) {
            result.rejectValue("id", "error.user", e.getMessage());
            return user.getId() != null ? "admin-edit-user" : "admin-add-user";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        if (user == null) {
            return "redirect:/admin/users?error=UserNotFound";
        }
        model.addAttribute("user", user);
        return "admin-edit-user";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/products")
    public String manageProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "admin-products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin-add-product";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") @Valid Product product, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            SaveProductCommand command = new SaveProductCommand(product, productService);

            List<String> emails = emailService.getAllEmails();
            command.addObserver(new EmailNotificationObserver(emails, emailService, product));

            command.execute();
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully.");
        } catch (IllegalArgumentException e) {
            result.rejectValue("id", "error.product", e.getMessage());
            return product.getId() != null ? "admin-edit-product" : "admin-add-product";
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.findProductById(id);
        if (product == null) {
            return "redirect:/admin/products?error=ProductNotFound";
        }
        model.addAttribute("product", product);
        return "admin-edit-product";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/discount/{id}")
    public String applyDiscount(@PathVariable Long id, @RequestParam("discount") double discount, RedirectAttributes redirectAttributes) {
        try {
            productService.applyDiscount(id, discount);
            redirectAttributes.addFlashAttribute("success", "Discount applied successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "admin-products";
    }

    @GetMapping("/statics")
    public String viewStatistics(Model model) {

        List<Order> orders = orderService.findAllOrders();

        double totalRevenue = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();


        int totalOrders = orders.size();


        List<Object[]> popularItems = orderService.getPopularItemsforAdmin();

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("popularItems", popularItems);

        return "admin-statistics";
    }
}
