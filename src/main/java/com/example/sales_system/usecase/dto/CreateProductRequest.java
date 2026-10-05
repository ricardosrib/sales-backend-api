package com.example.sales_system.usecase.dto;

public class CreateProductRequest {
  private String description;
  private Double unitPrice;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }
}
