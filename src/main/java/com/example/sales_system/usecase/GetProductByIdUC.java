package com.example.sales_system.usecase;

import com.example.sales_system.domain.model.ProductModel;
import com.example.sales_system.domain.services.StockService;
import com.example.sales_system.exception.NotFoundException;
import com.example.sales_system.usecase.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetProductByIdUC {
  private StockService stockService;

  @Autowired
  public GetProductByIdUC(StockService stockService) {
    this.stockService = stockService;
  }

  public ProductDTO run(long productId) {
    ProductModel product = stockService.productById(productId);
    if (product == null) {
      throw new NotFoundException("Product not found with ID: " + productId);
    }
    return ProductDTO.fromModel(product);
  }
}
