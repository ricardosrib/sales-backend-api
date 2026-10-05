package com.example.sales_system.repository;

import com.example.sales_system.domain.model.ProductModel;
import com.example.sales_system.domain.model.StockItemModel;
import com.example.sales_system.repository.jpa_entities.ProductEntity;
import com.example.sales_system.repository.jpa_entities.StockItemEntity;
import com.example.sales_system.repository.jpa_interface.StockJPA;
import com.example.sales_system.repository.repository_interface.IStockRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class StockJPARepository implements IStockRepository {
    private StockJPA stockRepository;

    public StockJPARepository(StockJPA stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<ProductModel> findAll() {
        List<StockItemEntity> items = stockRepository.findAll();
        return items.stream()
                .map(item -> ProductEntity.toProductModel(item.getProduct()))
                .toList();
    }

    @Override
    public List<ProductModel> findAllWithStock() {
        List<StockItemEntity> items = stockRepository.findAll();
        return items.stream()
                .filter(item -> item.getQuantity() > 0)
                .map(item -> ProductEntity.toProductModel(item.getProduct()))
                .toList();
    }

    @Override
    public int getStockQuantity(long id) {
        StockItemEntity item = stockRepository.findByProductId(id).orElse(null);
        return (item == null) ? -1 : item.getQuantity();
    }

    @Override
    public StockItemModel findById(long id) {
        StockItemEntity item = stockRepository.findByProductId(id).orElse(null);
        return (item == null) ? null : StockItemEntity.toStockItemModel(item);
    }

    @Override
    public void save(StockItemModel item) {
        StockItemEntity stockItem = StockItemEntity.fromStockItemModel(item);
        stockRepository.save(stockItem);
    }

    @Override
    public List<StockItemModel> findAllStockItems() {
        return stockRepository.findAll().stream()
                .map(StockItemEntity::toStockItemModel)
                .toList();
    }
}
