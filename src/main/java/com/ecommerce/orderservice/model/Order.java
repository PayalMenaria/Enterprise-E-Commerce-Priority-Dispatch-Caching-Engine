package com.ecommerce.orderservice.model;

public class Order {
    private String orderId;
    private String customerName;
    private double amount;
    private boolean isExpress;
    private long timestamp;

    public Order() {
        this.timestamp = System.currentTimeMillis();
    }

    public Order(String orderId, String customerName, double amount, boolean isExpress) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
        this.isExpress = isExpress;
        this.timestamp = System.currentTimeMillis();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isExpress() {
        return isExpress;
    }

    public void setExpress(boolean express) {
        isExpress = express;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
