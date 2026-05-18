package com.tareg.onlineshopping.model;

import java.math.BigDecimal;

public class CartItem {
    private long id;
    private long cartId;
    private long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private int availableStock;

    public CartItem(long id, long cartId, long productId, String productName,
                    BigDecimal unitPrice, int quantity, int availableStock) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.availableStock = availableStock;
    }

    public long getId() { return id; }
    public long getCartId() { return cartId; }
    public long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public int getAvailableStock() { return availableStock; }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}

