package com.project.Lanch.command;

import com.project.Lanch.model.Product;
import com.project.Lanch.observer.Observer;
import com.project.Lanch.service.ProductService;

import java.util.ArrayList;
import java.util.List;


/**
 * SaveProductCommand is responsible for saving a product using ProductService.
 * It supports both creating new products and updating existing ones.
 * Observers are notified with a message whenever the command is executed.
 */


public class SaveProductCommand implements Command<Product> {
    private final Product product;
    private final ProductService productService;
    private final List<Observer> observers = new ArrayList<>();

    public SaveProductCommand(Product product, ProductService productService) {
        this.product = product;
        this.productService = productService;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public Product execute() {
        Product savedProduct;
        if (product.getId() != null) {
            Product productFromDb = productService.findProductById(product.getId());
            if (productFromDb != null) {
                productFromDb.setName(product.getName());
                productFromDb.setDescription(product.getDescription());
                productFromDb.setPrice(product.getPrice());
                productFromDb.setQuantity(product.getQuantity());
                productFromDb.setImageUrl(product.getImageUrl());
                productFromDb.setSale(product.isSale());
                productFromDb.setRating(product.getRating());
                productFromDb.setOriginalPrice(product.getOriginalPrice());
                savedProduct = productService.saveAndReturnProduct(productFromDb);
            } else {
                throw new IllegalArgumentException("Product with ID " + product.getId() + " not found.");
            }
        } else {
            savedProduct = productService.saveAndReturnProduct(product);
        }

        String notificationMessage = String.format("Product saved: %s (Price: $%.2f)",
                savedProduct.getName(),
                savedProduct.getPrice());
        notifyObservers(notificationMessage);

        return savedProduct;
    }
}
