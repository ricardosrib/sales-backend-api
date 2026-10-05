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
import java.util.Comparator;

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

        if (budget.isFinalized() || budget.isCancelled()) {
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

    public List<BudgetModel> listBudgets(Long customerId, Boolean finalized, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new BadRequestException("fromDate must be on or before toDate");
        }
        return budgets.findAll().stream()
                .filter(b -> customerId == null || (b.getCustomer() != null && b.getCustomer().getId() == customerId))
                .filter(b -> finalized == null || b.isFinalized() == finalized)
                .filter(b -> from == null || !b.getBudgetDate().isBefore(from))
                .filter(b -> to == null || !b.getBudgetDate().isAfter(to))
                .sorted(Comparator.comparing(BudgetModel::getId).reversed())
                .toList();
    }

    public List<BudgetModel> listCustomerBudgets(long customerId) {
        if (customers.findById(customerId) == null) throw new NotFoundException("Customer not found: " + customerId);
        return listBudgets(customerId, null, null, null);
    }

    public void deleteBudget(long id) {
        BudgetModel budget = requireBudget(id);
        if (budget.isFinalized() || budget.isCancelled()) {
            throw new BadRequestException("Only active, unconfirmed budgets can be deleted");
        }
        budgets.deleteById(id);
    }

    public BudgetModel cancelBudget(long id) {
        BudgetModel budget = requireBudget(id);
        if (budget.isFinalized() || budget.isCancelled()) {
            throw new BadRequestException("Only active, unconfirmed budgets can be cancelled");
        }
        budget.cancel();
        budgets.save(budget);
        return budget;
    }

    public BudgetModel duplicateBudget(long id) {
        BudgetModel source = requireBudget(id);
        OrderModel order = new OrderModel(0);
        for (OrderItemModel item : source.getItems()) {
            ProductModel product = stockService.productById(item.getProduct().getId());
            if (product == null) throw new NotFoundException("Product not found with ID: " + item.getProduct().getId());
            order.addItem(new OrderItemModel(product, item.getQuantity()));
        }
        return createBudget(source.getCustomer().getId(), order);
    }

    private BudgetModel requireBudget(long id) {
        BudgetModel budget = budgets.findById(id);
        if (budget == null) throw new NotFoundException("Budget not found: " + id);
        return budget;
    }
}
