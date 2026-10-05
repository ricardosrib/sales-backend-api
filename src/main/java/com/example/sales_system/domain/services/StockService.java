package com.example.sales_system.domain.services;

import com.example.sales_system.domain.model.ProductModel;
import com.example.sales_system.domain.model.StockItemModel;
import com.example.sales_system.repository.repository_interface.IProductRepository;
import com.example.sales_system.repository.repository_interface.IStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.sales_system.exception.BadRequestException;
import com.example.sales_system.exception.NotFoundException;

@Service
public class StockService {
    private IStockRepository stock;
    private IProductRepository products;

    public StockService(IProductRepository products, IStockRepository stock) {
        this.products = products;
        this.stock = stock;
    }

    public List<ProductModel> allProducts() {
        return stock.findAll();
    }

    public List<ProductModel> availableProducts() {
        return stock.findAllWithStock();
    }

    public List<StockItemModel> allStockItems() {
        return stock.findAllStockItems();
    }

    public List<StockItemModel> lowStockItems() {
        return stock.findAllStockItems().stream()
                .filter(item -> item.getQuantity() <= item.getMinStock())
                .toList();
    }

    public ProductModel productById(long id) {
        return this.products.findById(id);
    }

    public int stockQuantity(long id) {
        int quantity = stock.getStockQuantity(id);
        if (quantity < 0) {
            throw new NotFoundException("Stock not found for product ID: " + id);
        }
        return quantity;
    }

    public StockItemModel addStock(long id, int quantity) {
        StockItemModel item = stock.findById(id);
        if (item == null) {
            return null;
        }
        if (quantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }
        item.setQuantity(Math.min(quantity, item.getMaxStock()));
        stock.save(item);
        return item;
    }

    public void deductStock(long id, int quantity) {
        StockItemModel item = stock.findById(id);
        if (item == null) {
            throw new NotFoundException("Product does not exist.");
        }
        if (item.getQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock quantity");
        }
        int newQuantity = item.getQuantity() - quantity;
        item.setQuantity(newQuantity);
        stock.save(item);
    }
}
