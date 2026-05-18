package com.tareg.onlineshopping.dao;

import com.tareg.onlineshopping.db.DBConnection;
import com.tareg.onlineshopping.model.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    public List<Address> findByUser(long userId) {
        String sql = "SELECT id, user_id, label, recipient_name, phone, line1, line2, city, state, postal_code, country, is_default " +
                "FROM addresses WHERE user_id=? ORDER BY is_default DESC, id DESC";
        List<Address> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error loading addresses: " + e.getMessage(), e);
        }
        return list;
    }

    public Address findByIdForUser(long addressId, long userId) {
        String sql = "SELECT id, user_id, label, recipient_name, phone, line1, line2, city, state, postal_code, country, is_default " +
                "FROM addresses WHERE id=? AND user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, addressId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error finding address: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean insert(long userId, String label, String recipientName, String phone,
                          String line1, String line2, String city, String state,
                          String postalCode, String country, boolean makeDefault) {
        String resetDefaultSql = "UPDATE addresses SET is_default = 0 WHERE user_id = ?";
        String insertSql = "INSERT INTO addresses (user_id, label, recipient_name, phone, line1, line2, city, state, postal_code, country, is_default) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (makeDefault || countByUser(conn, userId) == 0) {
                    try (PreparedStatement ps = conn.prepareStatement(resetDefaultSql)) {
                        ps.setLong(1, userId);
                        ps.executeUpdate();
                    }
                    makeDefault = true;
                }

                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setLong(1, userId);
                    ps.setString(2, label);
                    ps.setString(3, recipientName);
                    ps.setString(4, phone);
                    ps.setString(5, line1);
                    ps.setString(6, line2);
                    ps.setString(7, city);
                    ps.setString(8, state);
                    ps.setString(9, postalCode);
                    ps.setString(10, country);
                    ps.setBoolean(11, makeDefault);
                    boolean ok = ps.executeUpdate() == 1;
                    conn.commit();
                    return ok;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error adding address: " + e.getMessage(), e);
        }
    }

    public boolean setDefault(long addressId, long userId) {
        String resetSql = "UPDATE addresses SET is_default = 0 WHERE user_id = ?";
        String setSql = "UPDATE addresses SET is_default = 1 WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(resetSql)) {
                    ps.setLong(1, userId);
                    ps.executeUpdate();
                }
                int updated;
                try (PreparedStatement ps = conn.prepareStatement(setSql)) {
                    ps.setLong(1, addressId);
                    ps.setLong(2, userId);
                    updated = ps.executeUpdate();
                }
                conn.commit();
                return updated == 1;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error setting default address: " + e.getMessage(), e);
        }
    }

    public boolean delete(long addressId, long userId) {
        String sql = "DELETE FROM addresses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, addressId);
            ps.setLong(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("DB error deleting address: " + e.getMessage(), e);
        }
    }

    private int countByUser(Connection conn, long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM addresses WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private Address map(ResultSet rs) throws SQLException {
        return new Address(
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

