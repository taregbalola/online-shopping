package com.tareg.onlineshopping.model;

import java.math.BigDecimal;

public class ReportSummary {
    private int totalOrders;
    private BigDecimal totalRevenue;
    private int pendingOrders;

    public ReportSummary(int totalOrders, BigDecimal totalRevenue, int pendingOrders) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.pendingOrders = pendingOrders;
    }

    public int getTotalOrders() { return totalOrders; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public int getPendingOrders() { return pendingOrders; }
}

