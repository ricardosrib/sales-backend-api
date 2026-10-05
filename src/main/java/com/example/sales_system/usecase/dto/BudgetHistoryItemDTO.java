package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.OrderItemModel;

public class BudgetHistoryItemDTO {
    private final long productId;
    private final String productDescription;
    private final int quantity;
    private final double unitPrice;

    public BudgetHistoryItemDTO(long productId, String productDescription, int quantity, double unitPrice) {
        this.productId = productId;
        this.productDescription = productDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public long getProductId() { return productId; }
    public String getProductDescription() { return productDescription; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }

    public static BudgetHistoryItemDTO fromModel(OrderItemModel item) {
        return new BudgetHistoryItemDTO(item.getProduct().getId(), item.getProduct().getDescription(),
                item.getQuantity(), item.getProduct().getUnitPrice());
    }
}
