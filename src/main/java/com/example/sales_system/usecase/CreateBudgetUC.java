package com.example.sales_system.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sales_system.domain.model.BudgetModel;
import com.example.sales_system.domain.model.OrderItemModel;
import com.example.sales_system.domain.model.OrderModel;
import com.example.sales_system.domain.model.ProductModel;
import com.example.sales_system.domain.services.SalesService;
import com.example.sales_system.domain.services.StockService;
import com.example.sales_system.usecase.dto.BudgetDTO;
import com.example.sales_system.exception.BadRequestException;
import com.example.sales_system.exception.NotFoundException;
import com.example.sales_system.usecase.dto.OrderDTO;

@Component
public class CreateBudgetUC {
    private SalesService salesService;
    private StockService stockService;
    
    @Autowired
    public CreateBudgetUC(SalesService salesService, StockService stockService) {
        this.salesService = salesService;
        this.stockService = stockService;
    }

    public BudgetDTO run(long customerId, List<OrderDTO> items) {
        if (customerId <= 0) {
            throw new BadRequestException("Customer ID must be positive");
        }
        if (items == null || items.isEmpty()) {
            throw new BadRequestException("Items list cannot be null or empty");
        }
        
        OrderModel order = new OrderModel(0);
        for (OrderDTO item : items) {
            if (item == null) {
                throw new BadRequestException("Item cannot be null");
            }
            if (item.getQuantity() <= 0) {
                throw new BadRequestException("Item quantity must be positive");
            }
            if (item.getProductId() <= 0) {
                throw new BadRequestException("Product ID must be positive");
            }
            
            ProductModel product = stockService.productById(item.getProductId());
            if (product == null) {
                throw new NotFoundException("Product not found with ID: " + item.getProductId());
            }
            
            OrderItemModel orderItem = new OrderItemModel(product, item.getQuantity());
            order.addItem(orderItem);
        }
        BudgetModel budget = salesService.createBudget(customerId, order);
        return BudgetDTO.fromModel(budget);
    }
}
