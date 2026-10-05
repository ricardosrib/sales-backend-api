package com.example.sales_system.domain.model;

public class ProductModel {
    private long id;
    private String description;
    private double unitPrice;

    public ProductModel(long id, String description, double unitPrice) {
        this.id = id;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) { this.description = description; }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", description='" + getDescription() + "'" +
                ", unitPrice='" + getUnitPrice() + "'" +
                "}";
    }
}
