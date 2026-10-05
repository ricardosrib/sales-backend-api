package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.OrderItemModel;

public class OrderItemDTO {

  private long productId;
  private String productDescription;
  private double unitPrice;
  private int quantity;
  private double totalPrice;

  public OrderItemDTO(long productId, String productDescription, double unitPrice, int quantity) {
    this.productId = productId;
    this.productDescription = productDescription;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.totalPrice = unitPrice * quantity;
  }

  public long getProductId() {
    return productId;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public double getUnitPrice() {
    return unitPrice;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  @Override
  public String toString() {
    return "OrderItemDTO [productId="
        + productId
        + ", productDescription="
        + productDescription
        + ", unitPrice="
        + unitPrice
        + ", quantity="
        + quantity
        + ", totalPrice="
        + totalPrice
        + "]";
  }

  public static OrderItemDTO fromModel(OrderItemModel item) {
    if (item == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Order item model cannot be null");
    }
    if (item.getProduct() == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Product in order item cannot be null");
    }

    return new OrderItemDTO(
        item.getProduct().getId(),
        item.getProduct().getDescription(),
        item.getProduct().getUnitPrice(),
        item.getQuantity());
  }
}
