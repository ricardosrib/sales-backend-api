package com.example.sales_system.repository.jpa_entities;

import com.example.sales_system.domain.model.ProductModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;
    private double unitPrice;

    protected ProductEntity() {}

    public ProductEntity(long id, String description, double unitPrice) {
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

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", description='" + getDescription() + "'" +
                ", unitPrice='" + getUnitPrice() + "'" +
                "}";
    }

    public static ProductEntity fromProductModel(ProductModel model) {
        return new ProductEntity(model.getId(), model.getDescription(), model.getUnitPrice());
    }

    public static ProductModel toProductModel(ProductEntity product) {
        return new ProductModel(product.getId(), product.getDescription(), product.getUnitPrice());
    }
}
