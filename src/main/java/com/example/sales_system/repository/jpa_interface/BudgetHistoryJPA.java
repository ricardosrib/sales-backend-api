package com.example.sales_system.repository.jpa_interface;

import com.example.sales_system.repository.jpa_entities.BudgetHistoryEntity;
import java.util.List;
import org.springframework.data.repository.ListCrudRepository;

public interface BudgetHistoryJPA extends ListCrudRepository<BudgetHistoryEntity, Long> {
  List<BudgetHistoryEntity> findByBudgetIdOrderByOccurredAtAscIdAsc(long budgetId);

  void deleteByBudgetId(long budgetId);
}
