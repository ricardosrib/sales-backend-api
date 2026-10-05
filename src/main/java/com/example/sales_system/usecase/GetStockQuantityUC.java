package com.example.sales_system.usecase;

import com.example.sales_system.domain.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetStockQuantityUC {
  private StockService stockService;

  @Autowired
  public GetStockQuantityUC(StockService stockService) {
    this.stockService = stockService;
  }

  public int run(long productId) {
    return stockService.stockQuantity(productId);
  }
}
