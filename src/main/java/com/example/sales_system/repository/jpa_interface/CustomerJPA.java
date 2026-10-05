package com.example.sales_system.repository.jpa_interface;

import com.example.sales_system.repository.jpa_entities.CustomerEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface CustomerJPA extends ListCrudRepository<CustomerEntity, Long> {}
