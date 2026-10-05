package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.OrderItemModel;

public class OrderDTO {

  private long productId;
  private int quantity;

  public OrderDTO() {}

  public OrderDTO(long productId, int quantity) {
    if (productId <= 0) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Product ID must be positive");
    }
    if (quantity <= 0) {
      throw new com.example.sales_system.exception.BadRequestException("Quantity must be positive");
    }
    this.productId = productId;
    this.quantity = quantity;
  }

  public long getProductId() {
    return productId;
  }

  public void setProductId(long productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "RequestedItemDTO [productId=" + productId + ", quantity=" + quantity + "]";
  }

  public static OrderDTO fromModel(OrderItemModel item) {
    if (item == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Order item model cannot be null");
    }
    if (item.getProduct() == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Product in order item cannot be null");
    }
    return new OrderDTO(item.getProduct().getId(), item.getQuantity());
  }
}
