package com.project.Lanch.service;

import com.project.Lanch.decorator.DiscountDecorator;
import com.project.Lanch.decorator.ProductAdapter;
import com.project.Lanch.decorator.ProductComponent;
import com.project.Lanch.model.Product;
import com.project.Lanch.repository.OrderItemRepository;
import com.project.Lanch.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * ProductService manages product-related operations.
 * - Functions:
 *   - `findAllProducts`: Retrieves all products from the database.
 *   - `searchProducts`: Searches products by name or description containing the query string.
 *   - `findProductById`: Finds a product by its ID.
 *   - `deleteProduct`: Deletes a product by its ID.
 *   - `saveAndReturnProduct`: Saves and returns a product.
 *   - `applyDiscount`: Applies a discount to a product's price and manages original price tracking.
 *     - Ensures the discount is within the valid range (0-100%).
 *     - Sets the product to its original price if the discount is removed.
 * - Dependencies:
 *   - ProductRepository for database operations.
 * - Utilizes decorators (e.g., DiscountDecorator) to apply discounts dynamically.
 */


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    // this method is used to find all the products in the database but it is not used in the project so we need to find a way to use it

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }



    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product saveAndReturnProduct(Product product) {
        return productRepository.save(product);
    }

    public void applyDiscount(Long productId, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100%");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (discountPercentage == 0) {
            product.setPrice(product.getOriginalPrice() != null ? product.getOriginalPrice() : product.getPrice());
            product.setOriginalPrice(null);
            product.setSale(false);
        } else {
            if (product.getOriginalPrice() == null) {
                product.setOriginalPrice(product.getPrice());
            }
            double discountedPrice = product.getOriginalPrice() * (1 - discountPercentage / 100);
            product.setPrice(discountedPrice);
            product.setSale(true);
        }

        productRepository.save(product);
    }

}
