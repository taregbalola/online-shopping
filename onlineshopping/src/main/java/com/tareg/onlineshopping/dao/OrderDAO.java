package com.tareg.onlineshopping.dao;

import com.tareg.onlineshopping.db.DBConnection;
import com.tareg.onlineshopping.model.Address;
import com.tareg.onlineshopping.model.Order;
import com.tareg.onlineshopping.model.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public long checkoutCart(long userId, long addressId, String paymentMethod) {
        String findAddressSql = "SELECT id, user_id, label, recipient_name, phone, line1, line2, city, state, postal_code, country, is_default FROM addresses WHERE id = ? AND user_id = ?";
        String findCartSql = "SELECT id FROM carts WHERE user_id = ?";
        String findCartItemsSql = "SELECT ci.product_id, ci.quantity, p.name, p.price, p.stock FROM cart_items ci JOIN products p ON p.id = ci.product_id WHERE ci.cart_id = ? FOR UPDATE";
        String insertOrderSql = "INSERT INTO orders (user_id, total_amount, status, shipping_address, payment_method, payment_status) VALUES (?, ?, 'PENDING', ?, ?, 'PENDING')";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, line_total) VALUES (?, ?, ?, ?, ?)";
        String insertPaymentSql = "INSERT INTO payments (order_id, amount, payment_method, payment_status, transaction_ref) VALUES (?, ?, ?, 'PENDING', ?)";
        String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
        String clearCartSql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Address address = null;
                try (PreparedStatement ps = conn.prepareStatement(findAddressSql)) {
                    ps.setLong(1, addressId);
                    ps.setLong(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            address = new Address(
                                    rs.getLong("id"),
                                    rs.getLong("user_id"),
                                    rs.getString("label"),
                                    rs.getString("recipient_name"),
                                    rs.getString("phone"),
                                    rs.getString("line1"),
                                    rs.getString("line2"),
                                    rs.getString("city"),
                                    rs.getString("state"),
                                    rs.getString("postal_code"),
                                    rs.getString("country"),
                                    rs.getBoolean("is_default")
                            );
                        }
                    }
                }
                if (address == null) throw new IllegalStateException("Please select a valid shipping address.");

                long cartId = 0;
                try (PreparedStatement ps = conn.prepareStatement(findCartSql)) {
                    ps.setLong(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) cartId = rs.getLong("id");
                    }
                }
                if (cartId <= 0) throw new IllegalStateException("Your cart is empty.");

                class ItemRow {
                    long productId;
                    String productName;
                    int quantity;
                    int stock;
                    BigDecimal price;
                }
                List<ItemRow> cartItems = new ArrayList<>();
                BigDecimal totalAmount = BigDecimal.ZERO;

                try (PreparedStatement ps = conn.prepareStatement(findCartItemsSql)) {
                    ps.setLong(1, cartId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            ItemRow row = new ItemRow();
                            row.productId = rs.getLong("product_id");
                            row.productName = rs.getString("name");
                            row.quantity = rs.getInt("quantity");
                            row.stock = rs.getInt("stock");
                            row.price = rs.getBigDecimal("price");

                            if (row.quantity <= 0) {
                                throw new IllegalStateException("Invalid quantity in cart for product: " + row.productName);
                            }
                            if (row.stock < row.quantity) {
                                throw new IllegalStateException("Not enough stock for product: " + row.productName);
                            }
                            totalAmount = totalAmount.add(row.price.multiply(new BigDecimal(row.quantity)));
                            cartItems.add(row);
                        }
                    }
                }
                if (cartItems.isEmpty()) throw new IllegalStateException("Your cart is empty.");

                long orderId;
                try (PreparedStatement ps = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, userId);
                    ps.setBigDecimal(2, totalAmount);
                    ps.setString(3, address.asShippingText());
                    ps.setString(4, paymentMethod);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Failed to create order record.");
                        }
                        orderId = keys.getLong(1);
                    }
                }

                for (ItemRow row : cartItems) {
                    BigDecimal lineTotal = row.price.multiply(new BigDecimal(row.quantity));
                    try (PreparedStatement ps = conn.prepareStatement(insertItemSql)) {
                        ps.setLong(1, orderId);
                        ps.setLong(2, row.productId);
                        ps.setInt(3, row.quantity);
                        ps.setBigDecimal(4, row.price);
                        ps.setBigDecimal(5, lineTotal);
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = conn.prepareStatement(updateStockSql)) {
                        ps.setInt(1, row.quantity);
                        ps.setLong(2, row.productId);
                        ps.executeUpdate();
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(insertPaymentSql)) {
                    ps.setLong(1, orderId);
                    ps.setBigDecimal(2, totalAmount);
                    ps.setString(3, paymentMethod);
                    ps.setString(4, "PEND-" + System.currentTimeMillis());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(clearCartSql)) {
                    ps.setLong(1, cartId);
                    ps.executeUpdate();
                }

                conn.commit();
                return orderId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error while checking out cart: " + e.getMessage(), e);
        }
    }

    public List<Order> findByUser(long userId) {
        String sql = "SELECT o.id, o.user_id, u.full_name, o.total_amount, o.status, o.shipping_address, o.payment_method, o.payment_status, o.created_at " +
                "FROM orders o JOIN users u ON u.id = o.user_id WHERE o.user_id = ? ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItemsByOrder(conn, order.getId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading user orders: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Order> findAll() {
        String sql = "SELECT o.id, o.user_id, u.full_name, o.total_amount, o.status, o.shipping_address, o.payment_method, o.payment_status, o.created_at " +
                "FROM orders o JOIN users u ON u.id = o.user_id ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(findItemsByOrder(conn, order.getId()));
                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading all orders: " + e.getMessage(), e);
        }
        return list;
    }

    public Order findByIdAccessible(long orderId, long userId, boolean isAdmin) {
        String sql = "SELECT o.id, o.user_id, u.full_name, o.total_amount, o.status, o.shipping_address, o.payment_method, o.payment_status, o.created_at " +
                "FROM orders o JOIN users u ON u.id = o.user_id WHERE o.id = ?" + (isAdmin ? "" : " AND o.user_id = ?");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            if (!isAdmin) {
                ps.setLong(2, userId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItemsByOrder(conn, order.getId()));
                    return order;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading invoice order: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean updateOrderStatus(long orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setLong(2, orderId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("DB error updating order status: " + e.getMessage(), e);
        }
    }

    private List<OrderItem> findItemsByOrder(Connection conn, long orderId) throws SQLException {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, p.name AS product_name, oi.quantity, oi.unit_price, oi.line_total " +
                "FROM order_items oi JOIN products p ON p.id = oi.product_id WHERE oi.order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new OrderItem(
                            rs.getLong("id"),
                            rs.getLong("order_id"),
                            rs.getLong("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("unit_price"),
                            rs.getBigDecimal("line_total")
                    ));
                }
            }
        }
        return items;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("full_name"),
                rs.getBigDecimal("total_amount"),
                rs.getString("status"),
                rs.getString("shipping_address"),
                rs.getString("payment_method"),
                rs.getString("payment_status"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

