package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.BudgetModel;
import com.example.sales_system.domain.model.OrderItemModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetDTO {

  private long id;
  private List<OrderItemDTO> items;
  private double itemCost;
  private double tax;
  private double discount;
  private double consumerCost;
  private boolean finalized;
  private boolean cancelled;
  private LocalDate budgetDate;
  private LocalDate confirmationDate;

  public BudgetDTO(
      long id,
      List<OrderItemDTO> items,
      double itemCost,
      double tax,
      double discount,
      double consumerCost,
      boolean finalized,
      LocalDate budgetDate,
      LocalDate confirmationDate) {
    this.id = id;
    this.items = items;
    this.itemCost = itemCost;
    this.tax = tax;
    this.discount = discount;
    this.consumerCost = consumerCost;
    this.finalized = finalized;
    this.budgetDate = budgetDate;
    this.confirmationDate = confirmationDate;
  }

  public long getId() {
    return id;
  }

  public List<OrderItemDTO> getItems() {
    return items;
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

  public LocalDate getBudgetDate() {
    return budgetDate;
  }

  public LocalDate getConfirmationDate() {
    return confirmationDate;
  }

  public void finalize() {
    finalized = true;
  }

  public static BudgetDTO fromModel(BudgetModel budget) {
    if (budget == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Budget model cannot be null");
    }

    List<OrderItemDTO> items = new ArrayList<>(budget.getItems().size());
    for (OrderItemModel item : budget.getItems()) {
      items.add(OrderItemDTO.fromModel(item));
    }
    BudgetDTO dto =
        new BudgetDTO(
            budget.getId(),
            items,
            budget.getItemCost(),
            budget.getTax(),
            budget.getDiscount(),
            budget.getConsumerCost(),
            budget.isFinalized(),
            budget.getBudgetDate(),
            budget.getConfirmationDate());
    dto.cancelled = budget.isCancelled();
    return dto;
  }
}
