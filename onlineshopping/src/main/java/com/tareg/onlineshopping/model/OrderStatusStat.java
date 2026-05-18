package com.tareg.onlineshopping.model;

public class OrderStatusStat {
    private String status;
    private int total;

    public OrderStatusStat(String status, int total) {
        this.status = status;
        this.total = total;
    }

    public String getStatus() { return status; }
    public int getTotal() { return total; }
}

