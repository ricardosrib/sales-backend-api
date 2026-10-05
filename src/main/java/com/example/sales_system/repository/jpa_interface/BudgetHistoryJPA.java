package com.example.sales_system.repository.jpa_interface;

import com.example.sales_system.repository.jpa_entities.BudgetHistoryEntity;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface BudgetHistoryJPA extends ListCrudRepository<BudgetHistoryEntity, Long> {
    List<BudgetHistoryEntity> findByBudgetIdOrderByOccurredAtAscIdAsc(long budgetId);
    void deleteByBudgetId(long budgetId);
}
