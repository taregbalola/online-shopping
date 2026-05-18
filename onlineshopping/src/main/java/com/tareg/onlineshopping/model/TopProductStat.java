package com.tareg.onlineshopping.model;

import java.math.BigDecimal;

public class TopProductStat {
    private long productId;
    private String productName;
    private int quantitySold;
    private BigDecimal revenue;

    public TopProductStat(long productId, String productName, int quantitySold, BigDecimal revenue) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    public long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantitySold() { return quantitySold; }
    public BigDecimal getRevenue() { return revenue; }
}

