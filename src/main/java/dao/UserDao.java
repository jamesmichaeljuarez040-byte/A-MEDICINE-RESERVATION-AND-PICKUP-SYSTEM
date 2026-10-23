package dao;

import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

/**
 * Data access for the users table. All queries use PreparedStatement.
 */
public class UserDao {

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    /** Saves a new user. Status is always PENDING by default. */
    public boolean register(String username, String password, String roles) {
        String sql = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            ps.setString(3, roles);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    /** Verifies username and password. Returns the user if credentials match. */
    public Optional<User> login(String username, String password) {
        String sql = "SELECT id, username, password, roles, status FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("roles"),
                            rs.getString("status")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    /** Saves a user with an explicit status (used to seed the default admin). */
    public boolean registerWithStatus(String username, String password, String roles, String status) {
        String sql = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            ps.setString(3, roles);
            ps.setString(4, status);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    /** Returns all users with the given status, oldest first. */
    public List<User> findByStatus(String status) {
        String sql = "SELECT id, username, password, roles, status FROM users WHERE status = ? ORDER BY id";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("roles"),
                            rs.getString("status")));
                }
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    /** Changes a user's status (e.g. PENDING -> ACTIVE or REJECTED). */
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
