package com.example.sales_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

import com.example.sales_system.usecase.AvailableProductsUC;
import com.example.sales_system.usecase.ConfirmBudgetUC;
import com.example.sales_system.usecase.CreateBudgetUC;
import com.example.sales_system.usecase.GetBudgetByIdUC;
import com.example.sales_system.usecase.GetCustomerByIdUC;
import com.example.sales_system.usecase.GetProductByIdUC;
import com.example.sales_system.usecase.GetStockQuantityUC;
import com.example.sales_system.usecase.ListAllProductsUC;
import com.example.sales_system.usecase.BudgetManagementUC;
import com.example.sales_system.usecase.ListStockUC;
import com.example.sales_system.usecase.UpdateStockUC;
import com.example.sales_system.usecase.dto.BudgetDTO;
import com.example.sales_system.usecase.dto.CreateBudgetRequest;
import com.example.sales_system.usecase.dto.CustomerDTO;
import com.example.sales_system.usecase.dto.OrderDTO;
import com.example.sales_system.usecase.dto.ProductDTO;
import com.example.sales_system.usecase.dto.StockItemDTO;
import com.example.sales_system.usecase.dto.BudgetPageDTO;

@RestController
@RequestMapping("/api")
public class SalesController {

    private final ListAllProductsUC listAllProductsUC;
    private final AvailableProductsUC availableProductsUC;
    private final GetProductByIdUC getProductByIdUC;
    private final GetStockQuantityUC getStockQuantityUC;
    private final GetCustomerByIdUC getCustomerByIdUC;
    private final GetBudgetByIdUC getBudgetByIdUC;
    private final CreateBudgetUC createBudgetUC;
    private final ConfirmBudgetUC confirmBudgetUC;
    private final UpdateStockUC updateStockUC;
    private final BudgetManagementUC budgetManagementUC;
    private final ListStockUC listStockUC;

    public SalesController(
            ListAllProductsUC listAllProductsUC,
            AvailableProductsUC availableProductsUC,
            GetProductByIdUC getProductByIdUC,
            GetStockQuantityUC getStockQuantityUC,
            GetCustomerByIdUC getCustomerByIdUC,
            GetBudgetByIdUC getBudgetByIdUC,
            CreateBudgetUC createBudgetUC,
            ConfirmBudgetUC confirmBudgetUC,
            UpdateStockUC updateStockUC,
            BudgetManagementUC budgetManagementUC,
            ListStockUC listStockUC) {
        this.listAllProductsUC = listAllProductsUC;
        this.availableProductsUC = availableProductsUC;
        this.getProductByIdUC = getProductByIdUC;
        this.getStockQuantityUC = getStockQuantityUC;
        this.getCustomerByIdUC = getCustomerByIdUC;
        this.getBudgetByIdUC = getBudgetByIdUC;
        this.createBudgetUC = createBudgetUC;
        this.confirmBudgetUC = confirmBudgetUC;
        this.updateStockUC = updateStockUC;
        this.budgetManagementUC = budgetManagementUC;
        this.listStockUC = listStockUC;
    }

    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return listAllProductsUC.run();
    }

    @GetMapping("/products/available")
    public List<ProductDTO> getAvailableProducts() {
        return availableProductsUC.run();
    }

    @GetMapping("/products/{id}")
    public ProductDTO getProductById(@PathVariable long id) {
        return getProductByIdUC.run(id);
    }

    @GetMapping("/products/{id}/stock")
    public int getProductStock(@PathVariable long id) {
        return getStockQuantityUC.run(id);
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomerById(@PathVariable long id) {
        return getCustomerByIdUC.run(id);
    }

    @GetMapping("/customers/{id}/budgets")
    public BudgetPageDTO getCustomerBudgets(@PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return budgetManagementUC.customerBudgets(id, page, size);
    }

    @GetMapping("/budgets")
    public BudgetPageDTO listBudgets(@RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Boolean finalized,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return budgetManagementUC.list(customerId, finalized, fromDate, toDate, page, size);
    }

    @GetMapping("/budgets/{id}")
    public BudgetDTO getBudgetById(@PathVariable long id) {
        return getBudgetByIdUC.run(id);
    }

    @DeleteMapping("/budgets/{id}")
    public void deleteBudget(@PathVariable long id) { budgetManagementUC.delete(id); }

    @PostMapping("/budgets/{id}/cancel")
    public BudgetDTO cancelBudget(@PathVariable long id) { return budgetManagementUC.cancel(id); }

    @PostMapping("/budgets/{id}/duplicate")
    public BudgetDTO duplicateBudget(@PathVariable long id) { return budgetManagementUC.duplicate(id); }

    @PostMapping("/budgets")
    public BudgetDTO createBudget(@RequestBody CreateBudgetRequest request) {
        if (request == null) {
            throw new com.example.sales_system.exception.BadRequestException("Request body cannot be null");
        }
        return createBudgetUC.run(request.getCustomerId(), request.getItems());
    }

    @PostMapping("/budgets/{id}/confirm")
    public BudgetDTO confirmBudget(@PathVariable long id) {
        return confirmBudgetUC.run(id);
    }

    @PutMapping("/stocks/{id}")
    public StockItemDTO updateStock(@PathVariable long id, @RequestParam int quantity) {
        return updateStockUC.run(id, quantity);
    }

    @GetMapping("/stocks")
    public List<StockItemDTO> getAllStock() { return listStockUC.all(); }

    @GetMapping("/stocks/low")
    public List<StockItemDTO> getLowStock() { return listStockUC.low(); }
}
