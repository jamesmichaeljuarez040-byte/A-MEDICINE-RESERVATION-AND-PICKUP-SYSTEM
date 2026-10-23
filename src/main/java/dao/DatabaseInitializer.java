package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Makes sure every required table exists before the CLI menu starts.
 * Add new CREATE TABLE statements here (parents before children so
 * foreign keys can reference them).
 */
public class DatabaseInitializer {

    private static final String CREATE_USERS = """
            CREATE TABLE IF NOT EXISTS users (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                roles    TEXT NOT NULL CHECK (roles IN ('CUSTOMER', 'PHARMACIST', 'ADMIN')),
                status   TEXT NOT NULL DEFAULT 'PENDING'
            )
            """;

    private DatabaseInitializer() {
    }

    public static final String DEFAULT_ADMIN_USERNAME = "admin";
    public static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_USERS);
<<<<<<< HEAD
            // Older databases used ACTIVE; the approved status is now APPROVED.
            stmt.executeUpdate("UPDATE users SET status = 'APPROVED' WHERE status = 'ACTIVE'");
=======
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
            seedDefaultAdmin();
            System.out.println("[DB] Database initialized. Tables are ready.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

<<<<<<< HEAD
    /** Creates the first APPROVED admin so someone can approve other accounts. */
    private static void seedDefaultAdmin() {
        UserDao userDao = new UserDao();
        if (!userDao.usernameExists(DEFAULT_ADMIN_USERNAME)) {
            userDao.registerWithStatus(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD, "ADMIN", "APPROVED");
=======
    /** Creates the first ACTIVE admin so someone can approve other accounts. */
    private static void seedDefaultAdmin() {
        UserDao userDao = new UserDao();
        if (!userDao.usernameExists(DEFAULT_ADMIN_USERNAME)) {
            userDao.registerWithStatus(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD, "ADMIN", "ACTIVE");
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
        }
    }
}
