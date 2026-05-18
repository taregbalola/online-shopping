package com.tareg.onlineshopping.model;

import java.math.BigDecimal;

public class OrderItem {
    private long id;
    private long orderId;
    private long productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public OrderItem(long id, long orderId, long productId, String productName,
                     int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public long getId() { return id; }
    public long getOrderId() { return orderId; }
    public long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getLineTotal() { return lineTotal; }
}

