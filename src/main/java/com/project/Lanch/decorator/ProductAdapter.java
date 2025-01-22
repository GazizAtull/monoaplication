package com.project.Lanch.decorator;

import com.project.Lanch.model.Product;


/**
 * ProductAdapter adapts a Product object to the ProductComponent interface.
 * - Allows a Product to be used where a ProductComponent is expected.
 * - Delegates method calls to the underlying Product object.
 * - Provides access to the original Product object via the getProduct() method.
 */



public class ProductAdapter implements ProductComponent {
    private final Product product;

    public ProductAdapter(Product product) {
        this.product = product;
    }

    @Override
    public Double getPrice() {
        return product.getPrice();
    }

    @Override
    public String getDescription() {
        return product.getDescription();
    }

    @Override
    public String getName() {
        return product.getName();
    }

    public Product getProduct() {
        return product;
    }
}
