package com.project.Lanch.service;

import com.project.Lanch.model.Order;
import com.project.Lanch.model.Product;
import com.project.Lanch.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Locale;


/**
 * EmailService handles the sending of various types of emails.
 * - Functions:
 *   - `sendOrderConfirmationEmail`: Sends a confirmation email for an order using a Thymeleaf template.
 *   - `sendEmail`: Sends a simple email with text content.
 *   - `getAllEmails`: Retrieves the email addresses of all users from their profiles.
 *   - `sendNewProductEmail`: Sends a notification email for a new product, using a Thymeleaf template.
 * - Uses:
 *   - `JavaMailSender` for sending emails.
 *   - `TemplateEngine` for generating email content from Thymeleaf templates.
 * - Handles cases where users or profiles may lack email information.
 * - Throws `MessagingException` for email-related errors.
 */


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserService userService;

    public void sendOrderConfirmationEmail(Order order) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String recipient = order.getUser().getUserProfile().getEmail();
        helper.setTo(recipient);
        helper.setSubject("Order Confirmation #" + order.getId());

        Context context = new Context(Locale.getDefault());
        context.setVariable("order", order);

        String html = templateEngine.process("orderConfirmation", context);
        helper.setText(html, true);

        mailSender.send(message);
    }



    public List<String> getAllEmails() {
        return userService.findAllUsers().stream()
                .map(user -> {
                    UserProfile userProfile = user.getUserProfile();
                    if (userProfile == null || userProfile.getEmail() == null) {
                        System.err.println("User without email: " + user.getUsername());
                        return null;
                    }
                    return userProfile.getEmail();
                })
                .filter(email -> email != null)
                .toList();
    }

    public void sendNewProductEmail(String to, Product product) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("✨ New Product: " + product.getName());

        Context context = new Context();
        context.setVariable("productName", product.getName());
        context.setVariable("price", product.getPrice());
        context.setVariable("description", product.getDescription());
        context.setVariable("imageUrl", product.getImageUrl());

        String htmlContent = templateEngine.process("newProductNotification", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
