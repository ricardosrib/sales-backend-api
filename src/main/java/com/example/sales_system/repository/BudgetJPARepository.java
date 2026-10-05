package com.example.sales_system.repository;

import com.example.sales_system.domain.model.BudgetModel;
import com.example.sales_system.repository.jpa_entities.BudgetEntity;
import com.example.sales_system.repository.jpa_interface.BudgetJPA;
import com.example.sales_system.repository.repository_interface.IBudgetRepository;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class BudgetJPARepository implements IBudgetRepository {
  private BudgetJPA budgetRepository;

  public BudgetJPARepository(BudgetJPA repository) {
    this.budgetRepository = repository;
  }

  @Override
  public List<BudgetModel> findAll() {
    var budgets = budgetRepository.findAll();
    return budgets.stream().map(BudgetEntity::toBudgetModel).toList();
  }

  @Override
  public BudgetModel register(BudgetModel budget) {
    var budgetJpa = BudgetEntity.fromBudgetModel(budget);
    var savedBudget = budgetRepository.save(budgetJpa);
    return BudgetEntity.toBudgetModel(savedBudget);
  }

  @Override
  public BudgetModel findById(long id) {
    var budget = budgetRepository.findById(id).orElse(null);
    return (budget == null) ? null : BudgetEntity.toBudgetModel(budget);
  }

  @Override
  public void save(BudgetModel budget) {
    var budgetJpa = BudgetEntity.fromBudgetModel(budget);
    budgetRepository.save(budgetJpa);
  }

  @Override
  public void deleteById(long id) {
    budgetRepository.deleteById(id);
  }
}
