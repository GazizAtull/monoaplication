package com.project.Lanch.observer;

import com.project.Lanch.model.Product;
import com.project.Lanch.service.EmailService;

import jakarta.mail.MessagingException;
import java.util.List;


/**
 * EmailNotificationObserver is an implementation of the Observer interface.
 * - Sends email notifications about a product update or creation.
 * - Fields:
 *   - List of recipient email addresses.
 *   - EmailService to handle email sending.
 *   - Product associated with the notification.
 * - On an update, sends emails to all recipients, leveraging EmailService.
 * - Handles MessagingException to avoid breaking the notification flow.
 */



public class EmailNotificationObserver implements Observer {

    private final List<String> emails;
    private final EmailService emailService;
    private final Product product;

    public EmailNotificationObserver(List<String> emails, EmailService emailService, Product product) {
        this.emails = emails;
        this.emailService = emailService;
        this.product = product;
    }

    @Override
    public void update(String message) {
        for (String email : emails) {
            try {
                emailService.sendNewProductEmail(email, product);
            } catch (MessagingException e) {
                System.err.println("Error sending email to " + email + ": " + e.getMessage());
            }
        }
    }
}
