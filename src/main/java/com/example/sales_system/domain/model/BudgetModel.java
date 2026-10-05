package com.example.sales_system.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BudgetModel {
    private long id;
    private List<OrderItemModel> items;
    private double itemCost;
    private double tax;
    private double discount;
    private double consumerCost;
    private boolean finalized;
    private boolean cancelled;
    private CustomerModel customer;
    private LocalDate quotationDate;
    private LocalDate confirmationDate;

    public BudgetModel(long id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.finalized = false;
    }

    public BudgetModel() {
        this.items = new ArrayList<>();
        this.finalized = false;
    }

    public void addOrderItems(OrderModel order) {
        items.addAll(order.getItems());
    }

    public void addOrderItem(OrderItemModel orderItem) {
        items.add(orderItem);
    }

    public void upsertOrderItem(OrderItemModel orderItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId() == orderItem.getProduct().getId()) {
                items.set(i, orderItem);
                return;
            }
        }
        items.add(orderItem);
    }

    public List<OrderItemModel> getItems() {
        return Collections.unmodifiableList(items);
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public double getItemCost() {
        return itemCost;
    }

    public void setItemCost(double itemCost){
        this.itemCost = itemCost;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax){
        this.tax = tax;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }

    public double getConsumerCost() {
        return consumerCost;
    }

    public void setConsumerCost(double consumerCost){
        this.consumerCost = consumerCost;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public boolean isCancelled() { return cancelled; }

    public void cancel() { cancelled = true; }

    public void finalizeBudget(){
        finalized = true;
    }

    public CustomerModel getCustomer() {
        return customer;
    }

    public LocalDate getBudgetDate() {
        return quotationDate;
    }

    public void setBudgetDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
    }

    public LocalDate getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(LocalDate confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
}
