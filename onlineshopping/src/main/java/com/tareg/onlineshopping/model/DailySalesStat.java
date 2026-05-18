package com.tareg.onlineshopping.model;

import java.math.BigDecimal;

public class DailySalesStat {
    private String salesDate;
    private int ordersCount;
    private BigDecimal revenue;

    public DailySalesStat(String salesDate, int ordersCount, BigDecimal revenue) {
        this.salesDate = salesDate;
        this.ordersCount = ordersCount;
        this.revenue = revenue;
    }

    public String getSalesDate() { return salesDate; }
    public int getOrdersCount() { return ordersCount; }
    public BigDecimal getRevenue() { return revenue; }
}

