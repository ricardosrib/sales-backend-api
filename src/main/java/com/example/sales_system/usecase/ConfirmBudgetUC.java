package com.example.sales_system.usecase;

import com.example.sales_system.domain.model.BudgetModel;
import com.example.sales_system.domain.services.SalesService;
import com.example.sales_system.usecase.dto.BudgetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfirmBudgetUC {
  private SalesService salesService;

  @Autowired
  public ConfirmBudgetUC(SalesService salesService) {
    this.salesService = salesService;
  }

  public BudgetDTO run(long budgetId) {
    BudgetModel budget = salesService.confirmBudget(budgetId);
    return BudgetDTO.fromModel(budget);
  }
}
