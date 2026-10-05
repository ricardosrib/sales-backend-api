package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.CustomerModel;

public class CustomerDTO {

  private long id;
  private String name;
  private String country;
  private String state;

  public CustomerDTO(long id, String name, String country, String state) {
    this.id = id;
    this.name = name;
    this.country = country;
    this.state = state;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCountry() {
    return country;
  }

  public String getState() {
    return state;
  }

  @Override
  public String toString() {
    return "CustomerDTO [id="
        + id
        + ", name="
        + name
        + ", country="
        + country
        + ", state="
        + state
        + "]";
  }

  public static CustomerDTO fromModel(CustomerModel customer) {
    if (customer == null) {
      throw new com.example.sales_system.exception.BadRequestException(
          "Customer model cannot be null");
    }
    return new CustomerDTO(
        customer.getId(), customer.getName(), customer.getCountry(), customer.getState());
  }
}
