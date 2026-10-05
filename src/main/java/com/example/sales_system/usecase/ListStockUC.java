package com.example.sales_system.usecase;

import com.example.sales_system.domain.services.StockService;
import com.example.sales_system.usecase.dto.StockItemDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListStockUC {
  private final StockService stockService;

  public ListStockUC(StockService stockService) {
    this.stockService = stockService;
  }

  public List<StockItemDTO> all() {
    return stockService.allStockItems().stream().map(StockItemDTO::fromModel).toList();
  }

  public List<StockItemDTO> low() {
    return stockService.lowStockItems().stream().map(StockItemDTO::fromModel).toList();
  }
}
