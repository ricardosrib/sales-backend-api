package com.example.sales_system.usecase.dto;

import java.util.List;

public class CreateBudgetRequest {
  private long customerId;
  private List<OrderDTO> items;

  public CreateBudgetRequest() {}

  public CreateBudgetRequest(long customerId, List<OrderDTO> items) {
    this.customerId = customerId;
    this.items = items;
  }

  public long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }

  public List<OrderDTO> getItems() {
    return items;
  }

  public void setItems(List<OrderDTO> items) {
    this.items = items;
  }
}
