package com.example.sales_system.usecase;

import com.example.sales_system.domain.model.CustomerModel;
import com.example.sales_system.exception.NotFoundException;
import com.example.sales_system.repository.repository_interface.ICustomerRepository;
import com.example.sales_system.usecase.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetCustomerByIdUC {
  private ICustomerRepository customerRepository;

  @Autowired
  public GetCustomerByIdUC(ICustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public CustomerDTO run(long customerId) {
    CustomerModel customer = customerRepository.findById(customerId);
    if (customer == null) {
      throw new NotFoundException("Customer not found with ID: " + customerId);
    }
    return CustomerDTO.fromModel(customer);
  }
}
