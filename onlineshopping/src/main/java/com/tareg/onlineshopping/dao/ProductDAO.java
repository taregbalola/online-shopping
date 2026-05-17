package com.tareg.onlineshopping.dao;

import com.tareg.onlineshopping.db.DBConnection;
import com.tareg.onlineshopping.model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, name, description, category, price, stock, created_at FROM products ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading products: " + e.getMessage(), e);
        }
        return list;
    }

    public Product findById(long id) {
        String sql = "SELECT id, name, description, category, price, stock, created_at FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error finding product: " + e.getMessage(), e);
        }
        return null;
    }

    public Product insert(String name, String description, String category, BigDecimal price, int stock) {
        String sql = "INSERT INTO products (name, description, category, price, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, category);
            ps.setBigDecimal(4, price);
            ps.setInt(5, stock);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return findById(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error inserting product: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean update(long id, String name, String description, String category, BigDecimal price, int stock) {
        String sql = "UPDATE products SET name=?, description=?, category=?, price=?, stock=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, category);
            ps.setBigDecimal(4, price);
            ps.setInt(5, stock);
            ps.setLong(6, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("DB error updating product: " + e.getMessage(), e);
        }
    }

    public boolean delete(long id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("DB error deleting product: " + e.getMessage(), e);
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        return new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("category"),
            rs.getBigDecimal("price"),
            rs.getInt("stock"),
            rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

