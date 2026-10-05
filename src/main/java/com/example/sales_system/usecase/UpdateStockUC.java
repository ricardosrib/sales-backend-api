package com.example.sales_system.usecase;

import com.example.sales_system.domain.model.StockItemModel;
import com.example.sales_system.domain.services.StockService;
import com.example.sales_system.exception.BadRequestException;
import com.example.sales_system.exception.NotFoundException;
import com.example.sales_system.usecase.dto.StockItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateStockUC {
  private StockService stockService;

  @Autowired
  public UpdateStockUC(StockService stockService) {
    this.stockService = stockService;
  }

  public StockItemDTO run(long productId, int quantity) {
    if (productId <= 0) {
      throw new BadRequestException("Product ID must be positive");
    }
    if (quantity < 0) {
      throw new BadRequestException("Quantity cannot be negative");
    }

    StockItemModel stockItem = stockService.addStock(productId, quantity);
    if (stockItem == null) {
      throw new NotFoundException("Product not found with ID: " + productId);
    }
    return StockItemDTO.fromModel(stockItem);
  }
}
