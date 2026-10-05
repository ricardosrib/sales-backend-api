package com.example.sales_system.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderModel {
  private long id;
  private List<OrderItemModel> items;

  public OrderModel(long id) {
    this.id = id;
    this.items = new ArrayList<>();
  }

  public long getId() {
    return id;
  }

  public List<OrderItemModel> getItems() {
    return Collections.unmodifiableList(items);
  }

  public void addItem(OrderItemModel item) {
    items.add(item);
  }
}
