package com.project.Lanch.decorator;

/**
 * ProductDecorator is an abstract class that implements the ProductComponent interface.
 * - Acts as a base class for decorators, wrapping a ProductComponent to extend or modify its behavior.
 * - Delegates calls to the wrapped ProductComponent by default.
 */



public abstract class ProductDecorator implements ProductComponent {
    protected final ProductComponent decoratedProduct;

    public ProductDecorator(ProductComponent decoratedProduct) {
        this.decoratedProduct = decoratedProduct;
    }

    @Override
    public Double getPrice() {
        return decoratedProduct.getPrice();
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription();
    }

    @Override
    public String getName() {
        return decoratedProduct.getName();
    }
}
