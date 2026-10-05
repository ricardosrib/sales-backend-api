package com.example.sales_system.usecase;

import com.example.sales_system.domain.model.BudgetModel;
import com.example.sales_system.domain.services.SalesService;
import com.example.sales_system.exception.NotFoundException;
import com.example.sales_system.usecase.dto.BudgetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetBudgetByIdUC {
  private SalesService salesService;

  @Autowired
  public GetBudgetByIdUC(SalesService salesService) {
    this.salesService = salesService;
  }

  public BudgetDTO run(long budgetId) {
    BudgetModel budget = salesService.getBudgetById(budgetId);
    if (budget == null) {
      throw new NotFoundException("Budget not found with ID: " + budgetId);
    }
    return BudgetDTO.fromModel(budget);
  }
}
