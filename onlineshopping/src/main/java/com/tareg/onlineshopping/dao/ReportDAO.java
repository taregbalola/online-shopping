package com.tareg.onlineshopping.dao;

import com.tareg.onlineshopping.db.DBConnection;
import com.tareg.onlineshopping.model.DailySalesStat;
import com.tareg.onlineshopping.model.OrderStatusStat;
import com.tareg.onlineshopping.model.ReportSummary;
import com.tareg.onlineshopping.model.TopProductStat;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public ReportSummary getSummary() {
        String sql = "SELECT COUNT(*) AS total_orders, COALESCE(SUM(total_amount),0) AS revenue, " +
                "SUM(CASE WHEN status='PENDING' THEN 1 ELSE 0 END) AS pending_orders FROM orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ReportSummary(
                        rs.getInt("total_orders"),
                        rs.getBigDecimal("revenue"),
                        rs.getInt("pending_orders")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading report summary: " + e.getMessage(), e);
        }
        return new ReportSummary(0, BigDecimal.ZERO, 0);
    }

    public List<DailySalesStat> getDailySales(int lastDays) {
        String sql = "SELECT DATE(created_at) AS sales_date, COUNT(*) AS orders_count, COALESCE(SUM(total_amount),0) AS revenue " +
                "FROM orders WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                "GROUP BY DATE(created_at) ORDER BY sales_date DESC";
        List<DailySalesStat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lastDays);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DailySalesStat(
                            rs.getString("sales_date"),
                            rs.getInt("orders_count"),
                            rs.getBigDecimal("revenue")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading daily sales report: " + e.getMessage(), e);
        }
        return list;
    }

    public List<TopProductStat> getTopProducts(int limit) {
        String sql = "SELECT p.id AS product_id, p.name AS product_name, SUM(oi.quantity) AS qty_sold, COALESCE(SUM(oi.line_total),0) AS revenue " +
                "FROM order_items oi JOIN products p ON p.id = oi.product_id " +
                "GROUP BY p.id, p.name ORDER BY qty_sold DESC LIMIT ?";
        List<TopProductStat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TopProductStat(
                            rs.getLong("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("qty_sold"),
                            rs.getBigDecimal("revenue")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading top products report: " + e.getMessage(), e);
        }
        return list;
    }

    public List<OrderStatusStat> getOrderStatusBreakdown() {
        String sql = "SELECT status, COUNT(*) AS total FROM orders GROUP BY status ORDER BY total DESC";
        List<OrderStatusStat> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new OrderStatusStat(rs.getString("status"), rs.getInt("total")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading status breakdown: " + e.getMessage(), e);
        }
        return list;
    }
}

