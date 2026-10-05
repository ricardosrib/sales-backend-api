package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.StockItemModel;

public class StockItemDTO {

  private long id;
  private long productId;
  private String productDescription;
  private double unitPrice;
  private int quantity;
  private int minStock;
  private int maxStock;

  public StockItemDTO(
      long id,
      long productId,
      String productDescription,
      double unitPrice,
      int quantity,
      int minStock,
      int maxStock) {
    this.id = id;
    this.productId = productId;
    this.productDescription = productDescription;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.minStock = minStock;
    this.maxStock = maxStock;
  }

  public long getId() {
    return id;
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

  public int getMinStock() {
    return minStock;
  }

  public int getMaxStock() {
    return maxStock;
  }

  public boolean isLowStock() {
    return quantity <= minStock;
  }

  public boolean isAtMaxCapacity() {
    return quantity >= maxStock;
  }

  @Override
  public String toString() {
    return "StockItemDTO [id="
        + id
        + ", productId="
        + productId
        + ", productDescription="
        + productDescription
        + ", quantity="
        + quantity
        + ", minStock="
        + minStock
        + ", maxStock="
        + maxStock
        + "]";
  }

  public static StockItemDTO fromModel(StockItemModel stockItem) {
    if (stockItem == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Stock item model cannot be null");
    }
    if (stockItem.getProduct() == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Product in stock item cannot be null");
    }

    return new StockItemDTO(
        stockItem.getId(),
        stockItem.getProduct().getId(),
        stockItem.getProduct().getDescription(),
        stockItem.getProduct().getUnitPrice(),
        stockItem.getQuantity(),
        stockItem.getMinStock(),
        stockItem.getMaxStock());
  }
}
