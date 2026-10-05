package com.example.sales_system.domain.services;

import com.example.sales_system.domain.model.*;
import com.example.sales_system.exception.BadRequestException;
import com.example.sales_system.exception.NotFoundException;
import com.example.sales_system.repository.jpa_entities.BudgetHistoryEntity;
import com.example.sales_system.repository.jpa_interface.BudgetHistoryJPA;
import com.example.sales_system.repository.repository_interface.IBudgetRepository;
import com.example.sales_system.repository.repository_interface.ICustomerRepository;
import com.example.sales_system.repository.repository_interface.IProductRepository;
import com.example.sales_system.repository.repository_interface.IStockRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesService {
  private IBudgetRepository budgets;
  private IStockRepository stock;
  private ICustomerRepository customers;
  private StockService stockService;
  private IProductRepository products;
  private BudgetHistoryJPA history;
  private ObjectMapper objectMapper;

  public SalesService(
      IBudgetRepository budgets,
      IStockRepository stock,
      StockService stockService,
      ICustomerRepository customers,
      IProductRepository products,
      BudgetHistoryJPA history,
      ObjectMapper objectMapper) {
    this.budgets = budgets;
    this.stock = stock;
    this.stockService = stockService;
    this.customers = customers;
    this.products = products;
    this.history = history;
    this.objectMapper = objectMapper;
  }

  public List<ProductModel> availableProducts() {
    return stock.findAllWithStock();
  }

  public BudgetModel getBudgetById(long id) {
    return this.budgets.findById(id);
  }

  @Transactional
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
    double itemCost =
        newBudget.getItems().stream()
            .mapToDouble(it -> it.getProduct().getUnitPrice() * it.getQuantity())
            .sum();
    newBudget.setItemCost(itemCost);

    BudgetModel saved = this.budgets.register(newBudget);
    recordHistory(saved, "CREATED");
    return saved;
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
        throw new BadRequestException(
            "Insufficient stock for product: "
                + item.getProduct().getDescription()
                + ". Available: "
                + available
                + ", Requested: "
                + item.getQuantity());
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
    recordHistory(budget, "CONFIRMED");

    return budget;
  }

  public List<BudgetModel> listBudgets(
      Long customerId, Boolean finalized, LocalDate from, LocalDate to) {
    if (from != null && to != null && from.isAfter(to)) {
      throw new BadRequestException("fromDate must be on or before toDate");
    }
    return budgets.findAll().stream()
        .filter(
            b ->
                customerId == null
                    || (b.getCustomer() != null && b.getCustomer().getId() == customerId))
        .filter(b -> finalized == null || b.isFinalized() == finalized)
        .filter(b -> from == null || !b.getBudgetDate().isBefore(from))
        .filter(b -> to == null || !b.getBudgetDate().isAfter(to))
        .sorted(Comparator.comparing(BudgetModel::getId).reversed())
        .toList();
  }

  public List<BudgetModel> listCustomerBudgets(long customerId) {
    if (customers.findById(customerId) == null)
      throw new NotFoundException("Customer not found: " + customerId);
    return listBudgets(customerId, null, null, null);
  }

  @Transactional
  public void deleteBudget(long id) {
    BudgetModel budget = requireBudget(id);
    if (budget.isFinalized() || budget.isCancelled()) {
      throw new BadRequestException("Only active, unconfirmed budgets can be deleted");
    }
    history.deleteByBudgetId(id);
    budgets.deleteById(id);
  }

  @Transactional
  public BudgetModel cancelBudget(long id) {
    BudgetModel budget = requireBudget(id);
    if (budget.isFinalized() || budget.isCancelled()) {
      throw new BadRequestException("Only active, unconfirmed budgets can be cancelled");
    }
    budget.cancel();
    budgets.save(budget);
    recordHistory(budget, "CANCELLED");
    return budget;
  }

  @Transactional
  public BudgetModel addOrUpdateBudgetItem(long id, long productId, int quantity) {
    BudgetModel budget = requireBudget(id);
    ensureEditable(budget);
    if (productId <= 0 || quantity <= 0)
      throw new BadRequestException("Product ID and quantity must be positive");
    ProductModel product = products.findById(productId);
    if (product == null) throw new NotFoundException("Product not found with ID: " + productId);
    budget.upsertOrderItem(new OrderItemModel(product, quantity));
    recalculateCost(budget);
    budgets.save(budget);
    recordHistory(budget, "ITEM_ADDED_OR_UPDATED");
    return budget;
  }

  public List<BudgetHistoryEntity> budgetHistory(long id) {
    requireBudget(id);
    return history.findByBudgetIdOrderByOccurredAtAscIdAsc(id);
  }

  @Transactional
  public ProductModel createProduct(String description, Double unitPrice) {
    validateProduct(description, unitPrice);
    return products.save(new ProductModel(0, description.trim(), unitPrice));
  }

  @Transactional
  public ProductModel updateProduct(long id, String description, Double unitPrice) {
    if (id <= 0) throw new BadRequestException("Product ID must be positive");
    ProductModel product = products.findById(id);
    if (product == null) throw new NotFoundException("Product not found with ID: " + id);
    if (description != null) {
      if (description.isBlank()) throw new BadRequestException("Description cannot be blank");
      product.setDescription(description.trim());
    }
    if (unitPrice != null) {
      if (!Double.isFinite(unitPrice) || unitPrice <= 0)
        throw new BadRequestException("Unit price must be a positive finite number");
      product.setUnitPrice(unitPrice);
    }
    if (description == null && unitPrice == null)
      throw new BadRequestException("At least one product field must be provided");
    return products.save(product);
  }

  private void validateProduct(String description, Double unitPrice) {
    if (description == null || description.isBlank())
      throw new BadRequestException("Description is required");
    if (unitPrice == null || !Double.isFinite(unitPrice) || unitPrice <= 0)
      throw new BadRequestException("Unit price must be a positive finite number");
  }

  private void recalculateCost(BudgetModel budget) {
    budget.setItemCost(
        budget.getItems().stream()
            .mapToDouble(item -> item.getProduct().getUnitPrice() * item.getQuantity())
            .sum());
  }

  private void ensureEditable(BudgetModel budget) {
    if (budget.isFinalized() || budget.isCancelled())
      throw new BadRequestException("Only active, unconfirmed budgets can be changed");
  }

  private void recordHistory(BudgetModel budget, String action) {
    String status =
        budget.isCancelled() ? "CANCELLED" : budget.isFinalized() ? "CONFIRMED" : "DRAFT";
    String snapshot;
    try {
      snapshot =
          objectMapper.writeValueAsString(
              budget.getItems().stream()
                  .map(com.example.sales_system.usecase.dto.BudgetHistoryItemDTO::fromModel)
                  .toList());
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("Could not serialize budget history", ex);
    }
    history.save(
        new BudgetHistoryEntity(
            budget.getId(), Instant.now(), action, status, budget.getItemCost(), snapshot));
  }

  public BudgetModel duplicateBudget(long id) {
    BudgetModel source = requireBudget(id);
    OrderModel order = new OrderModel(0);
    for (OrderItemModel item : source.getItems()) {
      ProductModel product = stockService.productById(item.getProduct().getId());
      if (product == null)
        throw new NotFoundException("Product not found with ID: " + item.getProduct().getId());
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
