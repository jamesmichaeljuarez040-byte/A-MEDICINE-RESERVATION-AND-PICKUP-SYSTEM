package dao;

import model.ActivityLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDao {

    public void logAction(int userId, String actionType, String description) {
        String sql = "INSERT INTO activity_logs (user_id, action_type, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, actionType);
            ps.setString(3, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Log error: " + e.getMessage());
        }
    }

    public List<ActivityLog> getAllLogs() {
        return fetch("SELECT * FROM activity_logs ORDER BY timestamp DESC, id DESC", null);
    }

    public List<ActivityLog> getLogsByUserId(int userId) {
        return fetch("SELECT * FROM activity_logs WHERE user_id = ? ORDER BY timestamp DESC, id DESC", userId);
    }

    private List<ActivityLog> fetch(String sql, Integer userId) {
        List<ActivityLog> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId != null) {
                ps.setInt(1, userId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ActivityLog(rs.getInt("id"), rs.getInt("user_id"),
                            rs.getString("action_type"), rs.getString("description"),
                            rs.getString("timestamp")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Log query error: " + e.getMessage());
        }
        return list;
    }
}
