package com.example.sales_system.domain.services;

import com.example.sales_system.domain.model.*;
import com.example.sales_system.repository.repository_interface.IBudgetRepository;
import com.example.sales_system.repository.repository_interface.ICustomerRepository;
import com.example.sales_system.repository.repository_interface.IStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import com.example.sales_system.exception.BadRequestException;
import com.example.sales_system.exception.NotFoundException;
import java.util.List;

@Service
public class SalesService {
    private IBudgetRepository budgets;
    private IStockRepository stock;
    private ICustomerRepository customers;
    private StockService stockService;

    public SalesService(IBudgetRepository budgets,
                        IStockRepository stock,
                        StockService stockService,
                        ICustomerRepository customers) {
        this.budgets = budgets;
        this.stock = stock;
        this.stockService = stockService;
        this.customers = customers;
    }

    public List<ProductModel> availableProducts() {
        return stock.findAllWithStock();
    }

    public BudgetModel getBudgetById(long id) {
        return this.budgets.findById(id);
    }

    public BudgetModel createBudget(long customerId, OrderModel order) {
        System.out.println("Creating budget for customer ID: " + customerId + " with order: " + order);

        var newBudget = new BudgetModel();
        newBudget.addOrderItems(order);

        // Check if customer exists
        CustomerModel customer = this.customers.findById(customerId);
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }

        // Associate customer with budget
        newBudget.setCustomer(customer);
        newBudget.setBudgetDate(LocalDate.now());

        // Calculate item cost
        double itemCost = newBudget.getItems().stream()
                .mapToDouble(it -> it.getProduct().getUnitPrice() * it.getQuantity())
                .sum();
        newBudget.setItemCost(itemCost);

        return this.budgets.register(newBudget);
    }

    @Transactional
    public BudgetModel confirmBudget(long id) {
        var budget = this.budgets.findById(id);
        if (budget == null) {
            throw new NotFoundException("Budget not found: " + id);
        }

        if (budget.isFinalized()) {
            throw new BadRequestException("Budget already confirmed: " + id);
        }

        LocalDate expiration = budget.getBudgetDate().plusDays(21);
        if (LocalDate.now().isAfter(expiration)) {
            throw new BadRequestException("Budget expired on: " + expiration.toString());
        }

        // Check stock availability for all items
        for (OrderItemModel item : budget.getItems()) {
            int available = stock.getStockQuantity(item.getProduct().getId());
            if (available < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + item.getProduct().getDescription() +
                        ". Available: " + available + ", Requested: " + item.getQuantity());
            }
        }

        // Deduct stock if all items are available
        for (OrderItemModel item : budget.getItems()) {
            int available = stock.getStockQuantity(item.getProduct().getId());
            if (available >= item.getQuantity()) {
                stockService.deductStock(item.getProduct().getId(), item.getQuantity());
            }
        }

        budget.setConfirmationDate(LocalDate.now());
        budget.finalizeBudget();
        budgets.save(budget);

        return budget;
    }
}
