package com.example.sales_system.repository;

import com.example.sales_system.domain.model.CustomerModel;
import com.example.sales_system.repository.jpa_entities.CustomerEntity;
import com.example.sales_system.repository.jpa_interface.CustomerJPA;
import com.example.sales_system.repository.repository_interface.ICustomerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class CustomerJPARepository implements ICustomerRepository {
  private CustomerJPA customerRepository;

  public CustomerJPARepository(CustomerJPA customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public CustomerModel findById(long id) {
    CustomerEntity customer = customerRepository.findById(id).orElse(null);
    if (customer == null) {
      return null;
    } else {
      return CustomerEntity.toCustomerModel(customer);
    }
  }
}
