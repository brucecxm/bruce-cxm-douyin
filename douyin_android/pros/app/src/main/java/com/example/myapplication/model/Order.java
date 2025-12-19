package com.example.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private Date orderDate;
    private List<CartItem> items;
    private double totalAmount;
    private String status;
    private String address;
    private String phoneNumber;

    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = new Date();
        this.status = "待发货";
    }

    public Order(int id, List<CartItem> items, String address, String phoneNumber) {
        this.id = id;
        this.items = items != null ? items : new ArrayList<>();
        this.orderDate = new Date();
        this.status = "待发货";
        this.address = address;
        this.phoneNumber = phoneNumber;
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += item.getTotalPrice();
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        calculateTotalAmount();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
