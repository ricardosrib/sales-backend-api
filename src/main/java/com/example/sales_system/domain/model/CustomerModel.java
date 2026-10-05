package com.example.sales_system.domain.model;

public class CustomerModel {

  private long id;
  private String name;
  private String country;
  public String state;

  public CustomerModel(long id, String name, String country, String state) {
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
