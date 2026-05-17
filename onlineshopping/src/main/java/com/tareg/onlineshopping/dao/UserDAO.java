package com.tareg.onlineshopping.dao;

import com.tareg.onlineshopping.db.DBConnection;
import com.tareg.onlineshopping.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDAO {

    /**
     * Find user by username and verify password.
     * Returns the User on success, null on failure.
     */
    public User authenticate(String username, String plainPassword) {
        String sql = "SELECT id, username, password, full_name, role FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password");
                    if (BCrypt.checkpw(plainPassword, hash)) {
                        return new User(
                            rs.getLong("id"),
                            rs.getString("username"),
                            hash,
                            rs.getString("full_name"),
                            rs.getString("role")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error during authentication: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Register a new user with a BCrypt-hashed password.
     */
    public boolean register(String username, String plainPassword, String fullName, String role) {
        String sql = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            ps.setString(3, fullName);
            ps.setString(4, role);
            return ps.executeUpdate() == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // username already exists
        } catch (SQLException e) {
            throw new RuntimeException("DB error during registration: " + e.getMessage(), e);
        }
    }
}

