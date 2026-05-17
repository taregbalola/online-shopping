package com.tareg.onlineshopping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private final long id;
    private final String name;
    private final String description;
    private final String category;
    private final BigDecimal price;
    private final int stock;
    private final LocalDateTime createdAt;

    public Product(long id, String name, String description, String category, BigDecimal price, int stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

