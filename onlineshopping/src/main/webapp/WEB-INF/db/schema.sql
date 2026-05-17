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

-- Seed: default admin user  (password = admin123)
INSERT IGNORE INTO users (username, password, full_name, role)
VALUES ('admin', '$2a$12$SomeBcryptHashHerePleaseChange', 'Admin User', 'ADMIN');

-- Seed products
INSERT IGNORE INTO products (id, name, description, category, price, stock) VALUES
  (1, 'Smartphone',  'High-end smartphone with 5G support',             'Electronics', 999.00,  45),
  (2, 'Laptop',      'Professional laptop for work and study',           'Electronics', 1299.00, 28),
  (3, 'Headphones',  'Wireless headphones with noise cancellation',      'Accessories', 249.00,  156),
  (4, 'Smart Watch', 'Fitness tracker and notification watch',           'Wearables',   399.00,  92);

