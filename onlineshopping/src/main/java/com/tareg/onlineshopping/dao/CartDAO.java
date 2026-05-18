package com.tareg.onlineshopping.dao;

import com.tareg.onlineshopping.db.DBConnection;
import com.tareg.onlineshopping.model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public List<CartItem> findItemsByUser(long userId) {
        String sql = "SELECT ci.id, ci.cart_id, ci.product_id, ci.quantity, p.name, p.price, p.stock " +
                "FROM cart_items ci " +
                "JOIN carts c ON c.id = ci.cart_id " +
                "JOIN products p ON p.id = ci.product_id " +
                "WHERE c.user_id = ? ORDER BY ci.id DESC";
        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CartItem(
                            rs.getLong("id"),
                            rs.getLong("cart_id"),
                            rs.getLong("product_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            rs.getInt("quantity"),
                            rs.getInt("stock")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading cart: " + e.getMessage(), e);
        }
        return list;
    }

    public void addItem(long userId, long productId, int quantity) {
        String updateExistingSql = "UPDATE cart_items ci JOIN carts c ON c.id = ci.cart_id SET ci.quantity = ci.quantity + ? WHERE c.user_id = ? AND ci.product_id = ?";
        String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                long cartId = ensureCart(conn, userId);

                int updated;
                try (PreparedStatement ps = conn.prepareStatement(updateExistingSql)) {
                    ps.setInt(1, quantity);
                    ps.setLong(2, userId);
                    ps.setLong(3, productId);
                    updated = ps.executeUpdate();
                }

                if (updated == 0) {
                    try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                        ps.setLong(1, cartId);
                        ps.setLong(2, productId);
                        ps.setInt(3, quantity);
                        ps.executeUpdate();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error adding item to cart: " + e.getMessage(), e);
        }
    }

    public boolean updateQuantity(long userId, long itemId, int quantity) {
        String sql = "UPDATE cart_items ci JOIN carts c ON c.id = ci.cart_id SET ci.quantity = ? WHERE ci.id = ? AND c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, itemId);
            ps.setLong(3, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("DB error updating cart quantity: " + e.getMessage(), e);
        }
    }

    public boolean removeItem(long userId, long itemId) {
        String sql = "DELETE ci FROM cart_items ci JOIN carts c ON c.id = ci.cart_id WHERE ci.id = ? AND c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, itemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("DB error removing cart item: " + e.getMessage(), e);
        }
    }

    public long ensureCartForUser(long userId) {
        try (Connection conn = DBConnection.getConnection()) {
            return ensureCart(conn, userId);
        } catch (SQLException e) {
            throw new RuntimeException("DB error ensuring cart: " + e.getMessage(), e);
        }
    }

    private long ensureCart(Connection conn, long userId) throws SQLException {
        String findSql = "SELECT id FROM carts WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(findSql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        }

        String insertSql = "INSERT INTO carts (user_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        }
        throw new SQLException("Unable to create cart");
    }
}

