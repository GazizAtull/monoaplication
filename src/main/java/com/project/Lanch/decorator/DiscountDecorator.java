package com.project.Lanch.decorator;


/**
 * DiscountDecorator is a concrete implementation of ProductDecorator that applies
 * a discount to the price of a product and appends discount information to its description.
 * - Adjusts the price based on the given discount percentage.
 * - Enhances the product description to include the discount details.
 */


public class DiscountDecorator extends ProductDecorator {
    private final double discountPercentage;

    public DiscountDecorator(ProductComponent decoratedProduct, double discountPercentage) {
        super(decoratedProduct);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Double getPrice() {
        return decoratedProduct.getPrice() * (1 - discountPercentage / 100);
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription() + " (Discount: " + discountPercentage + "%)";
    }
}
