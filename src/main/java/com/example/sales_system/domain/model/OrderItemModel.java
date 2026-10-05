package com.example.sales_system.domain.model;

public class OrderItemModel {
  private ProductModel product;
  private int quantity;

  public OrderItemModel(ProductModel product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public ProductModel getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "OrderItem [product=" + product + ", quantity=" + quantity + "]";
  }
}
