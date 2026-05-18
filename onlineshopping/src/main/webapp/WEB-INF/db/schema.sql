-- ============================================================
-- Online Shopping Database Schema
-- Run this once in XAMPP phpMyAdmin or MySQL CLI
-- ============================================================

CREATE DATABASE IF NOT EXISTS onlineshopping
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE onlineshopping;

-- Users table (for login)
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT       AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,  -- BCrypt hash
    full_name  VARCHAR(200) NOT NULL,
    role       ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200)    NOT NULL,
    description TEXT,
    category    VARCHAR(100)    NOT NULL DEFAULT 'Other',
    price       DECIMAL(10,2)   NOT NULL,
    stock       INT             NOT NULL DEFAULT 0,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Carts table (one cart per user)
CREATE TABLE IF NOT EXISTS carts (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL UNIQUE,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_carts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Cart items table
CREATE TABLE IF NOT EXISTS cart_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id     BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL DEFAULT 1,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id),
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Customer addresses table
CREATE TABLE IF NOT EXISTS addresses (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    label           VARCHAR(60) NOT NULL DEFAULT 'Home',
    recipient_name  VARCHAR(200) NOT NULL,
    phone           VARCHAR(40) NOT NULL,
    line1           VARCHAR(255) NOT NULL,
    line2           VARCHAR(255),
    city            VARCHAR(120) NOT NULL,
    state           VARCHAR(120) NOT NULL,
    postal_code     VARCHAR(40) NOT NULL,
    country         VARCHAR(120) NOT NULL,
    is_default      TINYINT(1) NOT NULL DEFAULT 0,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT NOT NULL,
    total_amount      DECIMAL(10,2) NOT NULL,
    status            VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    shipping_address  VARCHAR(255) NOT NULL,
    payment_method    VARCHAR(40) NOT NULL,
    payment_status    VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Order items table
CREATE TABLE IF NOT EXISTS order_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL,
    unit_price  DECIMAL(10,2) NOT NULL,
    line_total  DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id         BIGINT NOT NULL,
    amount           DECIMAL(10,2) NOT NULL,
    payment_method   VARCHAR(40) NOT NULL,
    payment_status   VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    transaction_ref  VARCHAR(80) NOT NULL,
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_payments_ref UNIQUE (transaction_ref),
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_orders_user_created ON orders(user_id, created_at);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
CREATE INDEX idx_addresses_user ON addresses(user_id);

-- Seed: default admin user  (password = admin123)
INSERT IGNORE INTO users (username, password, full_name, role)
VALUES ('admin', '$2a$12$SomeBcryptHashHerePleaseChange', 'Admin User', 'ADMIN');

-- Seed products
INSERT IGNORE INTO products (id, name, description, category, price, stock) VALUES
  (1, 'Smartphone',  'High-end smartphone with 5G support',             'Electronics', 999.00,  45),
  (2, 'Laptop',      'Professional laptop for work and study',           'Electronics', 1299.00, 28),
  (3, 'Headphones',  'Wireless headphones with noise cancellation',      'Accessories', 249.00,  156),
  (4, 'Smart Watch', 'Fitness tracker and notification watch',           'Wearables',   399.00,  92);

