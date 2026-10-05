package com.example.sales_system.repository.jpa_entities;

import com.example.sales_system.domain.model.OrderItemModel;
import com.example.sales_system.domain.model.ProductModel;
import jakarta.persistence.*;

@Entity
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private ProductEntity product;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private BudgetEntity budget;

    private int quantity;
    private String productDescription;
    private double unitPrice;

    protected OrderItemEntity() {
    }

    public OrderItemEntity(ProductEntity product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.productDescription = product.getDescription();
        this.unitPrice = product.getUnitPrice();
    }

    public long getId() {
        return id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductDescription() { return productDescription; }

    public double getUnitPrice() { return unitPrice; }

    @Override
    public String toString() {
        return "OrderItem [product=" + product + ", quantity=" + quantity + "]";
    }

    public static OrderItemEntity fromOrderItemModel(OrderItemModel model) {
        ProductEntity product = ProductEntity.fromProductModel(model.getProduct());
        OrderItemEntity entity = new OrderItemEntity(product, model.getQuantity());
        entity.productDescription = model.getProduct().getDescription();
        entity.unitPrice = model.getProduct().getUnitPrice();
        return entity;
    }

    public static OrderItemModel toOrderItemModel(OrderItemEntity item) {
        ProductModel productModel = new ProductModel(item.getProduct().getId(), item.getProductDescription(), item.getUnitPrice());
        return new OrderItemModel(productModel, item.getQuantity());
    }
}
