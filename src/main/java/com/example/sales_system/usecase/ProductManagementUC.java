package com.example.sales_system.usecase;

import com.example.sales_system.domain.services.SalesService;
import com.example.sales_system.usecase.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductManagementUC {
  private final SalesService salesService;

  public ProductManagementUC(SalesService salesService) {
    this.salesService = salesService;
  }

  public ProductDTO create(String description, Double unitPrice) {
    return ProductDTO.fromModel(salesService.createProduct(description, unitPrice));
  }

  public ProductDTO update(long id, String description, Double unitPrice) {
    return ProductDTO.fromModel(salesService.updateProduct(id, description, unitPrice));
  }
}
