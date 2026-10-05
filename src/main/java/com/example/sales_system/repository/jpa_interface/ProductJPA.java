package com.example.sales_system.repository.jpa_interface;

import com.example.sales_system.repository.jpa_entities.ProductEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductJPA extends ListCrudRepository<ProductEntity, Long> {}
