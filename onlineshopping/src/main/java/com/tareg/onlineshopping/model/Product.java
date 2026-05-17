package com.tareg.onlineshopping.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private int stock;
    private LocalDateTime createdAt;

    public Product(long id, String name, String description, String category,
                   BigDecimal price, int stock, LocalDateTime createdAt) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.category    = category;
        this.price       = price;
        this.stock       = stock;
        this.createdAt   = createdAt;
    }

    public long          getId()          { return id; }
    public String        getName()        { return name; }
    public String        getDescription() { return description; }
    public String        getCategory()    { return category; }
    public BigDecimal    getPrice()       { return price; }
    public int           getStock()       { return stock; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
}

