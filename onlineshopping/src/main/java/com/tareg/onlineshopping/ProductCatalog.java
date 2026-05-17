package com.tareg.onlineshopping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public final class ProductCatalog {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1000);
    private static final CopyOnWriteArrayList<Product> PRODUCTS = new CopyOnWriteArrayList<>();

    static {
        seed("Smartphone", "High-end smartphone with 5G support", "Electronics", new BigDecimal("999.00"), 45);
        seed("Laptop", "Professional laptop for work and study", "Electronics", new BigDecimal("1299.00"), 28);
        seed("Headphones", "Wireless headphones with noise cancellation", "Accessories", new BigDecimal("249.00"), 156);
        seed("Smart Watch", "Fitness tracker and notification watch", "Wearables", new BigDecimal("399.00"), 92);
    }

    private ProductCatalog() {
    }

    private static void seed(String name, String description, String category, BigDecimal price, int stock) {
        PRODUCTS.add(new Product(ID_GENERATOR.getAndIncrement(), name, description, category, price, stock, LocalDateTime.now()));
    }

    public static void addProduct(String name, String description, String category, BigDecimal price, int stock) {
        Product product = new Product(ID_GENERATOR.getAndIncrement(), name, description, category, price, stock, LocalDateTime.now());
        PRODUCTS.add(0, product);
    }

    public static List<Product> getProducts() {
        return Collections.unmodifiableList(new ArrayList<>(PRODUCTS));
    }
}
