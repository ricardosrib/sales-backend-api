package com.example.sales_system.repository.repository_interface;

import com.example.sales_system.domain.model.BudgetModel;
import java.util.List;

public interface IBudgetRepository {
  List<BudgetModel> findAll();

  BudgetModel register(BudgetModel budget);

  BudgetModel findById(long id);

  void save(BudgetModel budget);

  void deleteById(long id);
}
