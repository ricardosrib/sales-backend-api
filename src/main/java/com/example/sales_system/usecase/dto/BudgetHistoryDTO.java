package com.example.sales_system.usecase.dto;

import com.example.sales_system.repository.jpa_entities.BudgetHistoryEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BudgetHistoryDTO {
  private final long id;
  private final Instant occurredAt;
  private final String action;
  private final String status;
  private final double itemCost;
  private final List<BudgetHistoryItemDTO> items;

  public BudgetHistoryDTO(BudgetHistoryEntity event, ObjectMapper objectMapper) {
    this.id = event.getId();
    this.occurredAt = event.getOccurredAt();
    this.action = event.getAction();
    this.status = event.getStatus();
    this.itemCost = event.getItemCost();
    this.items = new ArrayList<>();
    try {
      JsonNode snapshots = objectMapper.readTree(event.getItemSnapshot());
      for (JsonNode item : snapshots) {
        items.add(
            new BudgetHistoryItemDTO(
                item.path("productId").asLong(),
                item.path("productDescription").asText(),
                item.path("quantity").asInt(),
                item.path("unitPrice").asDouble()));
      }
    } catch (Exception ex) {
      throw new IllegalStateException("Could not read budget history snapshot", ex);
    }
  }

  public long getId() {
    return id;
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

  public List<BudgetHistoryItemDTO> getItems() {
    return items;
  }
}
