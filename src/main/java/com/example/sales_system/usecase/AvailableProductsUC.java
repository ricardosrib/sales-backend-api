package com.example.sales_system.usecase;

import com.example.sales_system.domain.services.StockService;
import com.example.sales_system.usecase.dto.ProductDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AvailableProductsUC {
  private StockService stockService;

  @Autowired
  public AvailableProductsUC(StockService stockService) {
    this.stockService = stockService;
  }

  public List<ProductDTO> run() {
    return stockService.availableProducts().stream().map(p -> ProductDTO.fromModel(p)).toList();
  }
}
