package com.example.sales_system.repository;

import com.example.sales_system.domain.model.ProductModel;
import com.example.sales_system.repository.jpa_entities.ProductEntity;
import com.example.sales_system.repository.jpa_interface.ProductJPA;
import com.example.sales_system.repository.repository_interface.IProductRepository;
import java.util.LinkedList;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class ProductJPARepository implements IProductRepository {
  private ProductJPA productRepository;

  public ProductJPARepository(ProductJPA productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<ProductModel> findAll() {
    List<ProductEntity> products = productRepository.findAll();
    if (products.isEmpty()) {
      return new LinkedList<>();
    } else {
      return products.stream().map(ProductEntity::toProductModel).toList();
    }
  }

  @Override
  public ProductModel findById(long id) {
    ProductEntity product = productRepository.findById(id).orElse(null);
    return (product == null) ? null : ProductEntity.toProductModel(product);
  }

  @Override
  public ProductModel save(ProductModel product) {
    ProductEntity entity = ProductEntity.fromProductModel(product);
    return ProductEntity.toProductModel(productRepository.save(entity));
  }
}
