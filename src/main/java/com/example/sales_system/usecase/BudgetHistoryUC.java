package com.example.sales_system.usecase;

import com.example.sales_system.domain.services.SalesService;
import com.example.sales_system.usecase.dto.BudgetHistoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BudgetHistoryUC {
  private final SalesService salesService;
  private final ObjectMapper objectMapper;

  public BudgetHistoryUC(SalesService salesService, ObjectMapper objectMapper) {
    this.salesService = salesService;
    this.objectMapper = objectMapper;
  }

  public List<BudgetHistoryDTO> run(long budgetId) {
    return salesService.budgetHistory(budgetId).stream()
        .map(event -> new BudgetHistoryDTO(event, objectMapper))
        .toList();
  }
}
