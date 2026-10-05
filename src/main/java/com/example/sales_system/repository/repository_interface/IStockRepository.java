package com.example.sales_system.repository.repository_interface;

import com.example.sales_system.domain.model.ProductModel;
import com.example.sales_system.domain.model.StockItemModel;

import java.util.List;

public interface IStockRepository {
    List<ProductModel> findAll();
    List<ProductModel> findAllWithStock();
    int getStockQuantity(long id);
    StockItemModel findById(long id);
    void save(StockItemModel item);
    List<StockItemModel> findAllStockItems();
}
