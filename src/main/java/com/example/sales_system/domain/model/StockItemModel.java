package com.example.sales_system.domain.model;

public class StockItemModel {
  private long id;
  private ProductModel product;
  private int quantity;
  private int minStock;
  private int maxStock;

  public StockItemModel(long id, ProductModel product, int quantity, int minStock, int maxStock) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.minStock = minStock;
    this.maxStock = maxStock;
  }

  public long getId() {
    return id;
  }

  public ProductModel getProduct() {
    return product;
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

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setMinStock(int minStock) {
    this.minStock = minStock;
  }

  public void setMaxStock(int maxStock) {
    this.maxStock = maxStock;
  }

  @Override
  public String toString() {
    return "InventoryItem [id="
        + id
        + ", product="
        + product
        + ", quantity="
        + quantity
        + ", minStock="
        + minStock
        + ", maxStock="
        + maxStock
        + "]";
  }
}
