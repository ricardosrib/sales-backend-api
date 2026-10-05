package com.example.sales_system.repository.jpa_entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.Instant;

@Entity
public class BudgetHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private long budgetId;
  private Instant occurredAt;
  private String action;
  private String status;
  private double itemCost;
  @Lob private String itemSnapshot;

  protected BudgetHistoryEntity() {}

  public BudgetHistoryEntity(
      long budgetId,
      Instant occurredAt,
      String action,
      String status,
      double itemCost,
      String itemSnapshot) {
    this.budgetId = budgetId;
    this.occurredAt = occurredAt;
    this.action = action;
    this.status = status;
    this.itemCost = itemCost;
    this.itemSnapshot = itemSnapshot;
  }

  public long getId() {
    return id;
  }

  public long getBudgetId() {
    return budgetId;
  }

  public Instant getOccurredAt() {
    return occurredAt;
  }

  public String getAction() {
    return action;
  }

  public String getStatus() {
    return status;
  }

  public double getItemCost() {
    return itemCost;
  }

  public String getItemSnapshot() {
    return itemSnapshot;
  }
}
