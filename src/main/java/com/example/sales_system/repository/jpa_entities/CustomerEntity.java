package com.example.sales_system.repository.jpa_entities;

import com.example.sales_system.domain.model.CustomerModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class CustomerEntity {
  @Id private long id;
  private String name;
  private String country;
  public String state;

  @OneToMany(mappedBy = "customer")
  private List<BudgetEntity> budgets;

  protected CustomerEntity() {}

  public CustomerEntity(long id, String name, String country, String state) {
    this.id = id;
    this.name = name;
    this.country = country;
    this.state = state;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getCountry() {
    return this.country;
  }

  public String getState() {
    return this.state;
  }

  public static CustomerEntity fromCustomerModel(CustomerModel customerModel) {
    return new CustomerEntity(
        customerModel.getId(),
        customerModel.getName(),
        customerModel.getCountry(),
        customerModel.getState());
  }

  public static CustomerModel toCustomerModel(CustomerEntity customer) {
    return new CustomerModel(customer.id, customer.name, customer.country, customer.state);
  }

  @Override
  public String toString() {
    return "{"
        + " id='"
        + getId()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", country='"
        + getCountry()
        + "'"
        + ", state='"
        + getState()
        + "'"
        + "}";
  }
}
