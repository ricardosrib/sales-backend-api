package com.example.sales_system.repository.repository_interface;

import com.example.sales_system.domain.model.ProductModel;

import java.util.List;

public interface IProductRepository {
    List<ProductModel> findAll();
    ProductModel findById(long id);
    ProductModel save(ProductModel product);
}
