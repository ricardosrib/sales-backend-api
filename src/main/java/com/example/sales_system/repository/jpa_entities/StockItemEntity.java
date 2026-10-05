package com.example.sales_system.repository.jpa_entities;

import com.example.sales_system.domain.model.StockItemModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class StockItemEntity {
  @Id private long id;

  @ManyToOne(cascade = CascadeType.REFRESH)
  private ProductEntity product;

  private int quantity;
  private int minStock;
  private int maxStock;

  public StockItemEntity(long id, ProductEntity product, int quantity, int minStock, int maxStock) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.minStock = minStock;
    this.maxStock = maxStock;
  }

  protected StockItemEntity() {}

  public long getId() {
    return id;
  }

  public ProductEntity getProduct() {
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
    return "StockItem [id="
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

  public static StockItemModel toStockItemModel(StockItemEntity item) {
    return new StockItemModel(
        item.getId(),
        ProductEntity.toProductModel(item.getProduct()),
        item.getQuantity(),
        item.getMinStock(),
        item.getMaxStock());
  }

  public static StockItemEntity fromStockItemModel(StockItemModel model) {
    return new StockItemEntity(
        model.getId(),
        ProductEntity.fromProductModel(model.getProduct()),
        model.getQuantity(),
        model.getMinStock(),
        model.getMaxStock());
  }
}
