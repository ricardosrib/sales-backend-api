package com.example.sales_system.repository.jpa_entities;

import com.example.sales_system.domain.model.BudgetModel;
import com.example.sales_system.domain.model.OrderItemModel;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BudgetEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private double itemCost;
  private double tax;
  private double discount;
  private double consumerCost;
  private boolean finalized;
  private boolean cancelled;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItemEntity> items;

  @ManyToOne(cascade = CascadeType.REFRESH)
  private CustomerEntity customer;

  private LocalDate budgetDate;
  private LocalDate finalizationDate;

  protected BudgetEntity() {}

  protected BudgetEntity(
      long id,
      double itemCost,
      double tax,
      double discount,
      double consumerCost,
      boolean finalized,
      CustomerEntity customer,
      LocalDate budgetDate,
      LocalDate finalizationDate) {
    this.id = id;
    this.itemCost = itemCost;
    this.tax = tax;
    this.discount = discount;
    this.consumerCost = consumerCost;
    this.finalized = finalized;
    this.items = new ArrayList<>();
    this.customer = customer;
    this.budgetDate = budgetDate;
    this.finalizationDate = finalizationDate;
  }

  public void addOrderItem(OrderItemEntity item) {
    items.add(item);
  }

  public long getId() {
    return id;
  }

  public double getItemCost() {
    return itemCost;
  }

  public double getTax() {
    return tax;
  }

  public double getDiscount() {
    return discount;
  }

  public double getConsumerCost() {
    return consumerCost;
  }

  public boolean isFinalized() {
    return finalized;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public List<OrderItemEntity> getItems() {
    return items;
  }

  public void setItemCost(double itemCost) {
    this.itemCost = itemCost;
  }

  public void setTax(double tax) {
    this.tax = tax;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public void setConsumerCost(double consumerCost) {
    this.consumerCost = consumerCost;
  }

  public void setFinalized(boolean finalized) {
    this.finalized = finalized;
  }

  public CustomerEntity getCustomer() {
    return customer;
  }

  public LocalDate getBudgetDate() {
    return budgetDate;
  }

  public LocalDate getFinalizationDate() {
    return finalizationDate;
  }

  public static BudgetEntity fromBudgetModel(BudgetModel model) {
    BudgetEntity budget =
        new BudgetEntity(
            model.getId(),
            model.getItemCost(),
            model.getTax(),
            model.getDiscount(),
            model.getConsumerCost(),
            model.isFinalized(),
            CustomerEntity.fromCustomerModel(model.getCustomer()),
            model.getBudgetDate(),
            model.getConfirmationDate());
    budget.setCancelled(model.isCancelled());

    for (OrderItemModel item : model.getItems()) {
      budget.addOrderItem(OrderItemEntity.fromOrderItemModel(item));
    }

    return budget;
  }

  public static BudgetModel toBudgetModel(BudgetEntity budget) {
    BudgetModel model = new BudgetModel();
    model.setId(budget.getId());
    model.setItemCost(budget.getItemCost());
    model.setTax(budget.getTax());
    model.setDiscount(budget.getDiscount());
    model.setConsumerCost(budget.getConsumerCost());
    model.setCustomer(CustomerEntity.toCustomerModel(budget.getCustomer()));
    if (budget.isFinalized()) model.finalizeBudget();
    if (budget.isCancelled()) model.cancel();
    model.setBudgetDate(budget.getBudgetDate());
    model.setConfirmationDate(budget.getFinalizationDate());
    for (OrderItemEntity item : budget.getItems()) {
      model.addOrderItem(OrderItemEntity.toOrderItemModel(item));
    }
    return model;
  }
}
