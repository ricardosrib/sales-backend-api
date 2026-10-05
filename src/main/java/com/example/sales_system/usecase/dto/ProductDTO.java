package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.ProductModel;

public class ProductDTO {

  private long id;
  private String description;
  private double unitPrice;

  public ProductDTO(long id, String description, double unitPrice) {
    this.id = id;
    this.description = description;
    this.unitPrice = unitPrice;
  }

  public long getId() {
    return this.id;
  }

  public String getDescription() {
    return this.description;
  }

  public double getUnitPrice() {
    return this.unitPrice;
  }

  public static ProductDTO fromModel(ProductModel product) {
    if (product == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Product model cannot be null");
    }
    return new ProductDTO(product.getId(), product.getDescription(), product.getUnitPrice());
  }
}
