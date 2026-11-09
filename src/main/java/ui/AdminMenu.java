package ui;

import dao.ActivityLogDao;
import dao.UserDao;
import model.ActivityLog;
import model.Roles;
import model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner;
    private final UserDao userDao;
    private final ActivityLogDao logDao;
    private final User admin;

    public AdminMenu(Scanner scanner, UserDao userDao, ActivityLogDao logDao, User admin) {
        this.scanner = scanner;
        this.userDao = userDao;
        this.logDao = logDao;
        this.admin = admin;
    }

    public void show() {
        while (true) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Update User Role/Status");
            System.out.println("5. View System Audit Logs");
            System.out.println("0. Logout");
            int choice = readIntInput("Choose: ");
            switch (choice) {
                case 1 -> printUserTable(userDao.getAllUsers());
                case 2 -> printUserTable(userDao.getPendingUsers());
                case 3 -> approveUser();
                case 4 -> updateUser();
                case 5 -> auditLogMenu();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---------- Approve ----------
    private void approveUser() {
        List<User> pending = userDao.getPendingUsers();
        printUserTable(pending);
        if (pending.isEmpty()) return;

        int id = readIntInput("Enter User ID to approve (0 to cancel): ");
        if (id == 0) {
            System.out.println("Approval cancelled.");
            return;
        }
        boolean inPending = pending.stream().anyMatch(u -> u.id() == id);
        if (!inPending) {
            System.out.println("User ID " + id + " is not in the pending list.");
            return;
        }
        if (userDao.approveUser(id)) {
            logDao.logAction(admin.id(), "USER_APPROVED",
                    "Admin [" + admin.id() + "] approved account for User ID " + id);
            System.out.println("SUCCESS: User ID " + id + " has been APPROVED.");
        } else {
            System.out.println("Failed to approve user.");
        }
    }

    // ---------- Update ----------
    private void updateUser() {
        List<User> all = userDao.getAllUsers();
        printUserTable(all);
        int id = readIntInput("Enter User ID to update (0 to cancel): ");
        if (id == 0) {
            System.out.println("Update cancelled.");
            return;
        }
        if (id == admin.id()) {
            System.out.println("WARNING: Modifying the currently active admin account is restricted.");
            return;
        }
        if (all.stream().noneMatch(u -> u.id() == id)) {
            System.out.println("User ID " + id + " does not exist.");
            return;
        }
        String role = readValidRole();
        if (role == null) {
            System.out.println("Update cancelled.");
            return;
        }
        String status = readValidStatus();
        if (status == null) {
            System.out.println("Update cancelled.");
            return;
        }
        if (userDao.updateUserRoleAndStatus(id, role, status)) {
            logDao.logAction(admin.id(), "USER_UPDATED",
                    "Admin [" + admin.id() + "] changed User " + id + " to " + role + "/" + status);
            System.out.println("SUCCESS: User ID " + id + " updated to role " + role + ", status " + status + ".");
        } else {
            System.out.println("Failed to update user.");
        }
    }

    // ---------- Audit logs ----------
    private void auditLogMenu() {
        while (true) {
            System.out.println("\n--- System Audit Logs ---");
            System.out.println("1. View All System Logs");
            System.out.println("2. Filter Logs by User ID");
            System.out.println("0. Back");
            int c = readIntInput("Choose: ");
            switch (c) {
                case 1 -> printLogTable(logDao.getAllLogs());
                case 2 -> {
                    int uid = readIntInput("Enter User ID (0 to cancel): ");
                    if (uid != 0) {
                        printLogTable(logDao.getLogsByUserId(uid));
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---------- Input validation ----------
    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String in = scanner.nextLine().trim();
            try {
                return Integer.parseInt(in);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /** Returns a valid role, or null if the admin enters 0 to cancel. */
    private String readValidRole() {
        while (true) {
            System.out.print("Enter new role " + Roles.ALL + " (0 to cancel): ");
            String in = scanner.nextLine().trim().toUpperCase();
            if (in.equals("0")) return null;
            if (Roles.ALL.contains(in)) return in;
            System.out.println("Invalid role. Valid roles: " + Roles.ALL);
        }
    }

    /** Returns a valid status, or null if the admin enters 0 to cancel. */
    private String readValidStatus() {
        while (true) {
            System.out.print("Enter new status (APPROVED/PENDING, 0 to cancel): ");
            String in = scanner.nextLine().trim().toUpperCase();
            if (in.equals("0")) return null;
            if (in.equals("APPROVED") || in.equals("PENDING")) return in;
            System.out.println("Invalid status. Valid statuses: APPROVED, PENDING");
        }
    }

    // ---------- Table helpers ----------
    private void printUserTable(List<User> users) {
        String line = "+-------+-----------------+--------------+------------+";
        System.out.println(line);
        System.out.printf("| %-5s | %-15s | %-12s | %-10s |%n", "ID", "Username", "Role", "Status");
        System.out.println(line);
        if (users.isEmpty()) {
            System.out.printf("| %-47s |%n", "No records found.");
        }
        for (User u : users) {
            System.out.printf("| %-5d | %-15s | %-12s | %-10s |%n",
                    u.id(), cut(u.username(), 15), cut(u.role(), 12), cut(u.status(), 10));
        }
        System.out.println(line);
    }

    private void printLogTable(List<ActivityLog> logs) {
        String fmt = "%-5s | %-8s | %-18s | %-35s | %-20s%n";
        String line = "-".repeat(5 + 3 + 8 + 3 + 18 + 3 + 35 + 3 + 20);
        System.out.println(line);
        System.out.printf(fmt, "ID", "User ID", "Action", "Description", "Timestamp");
        System.out.println(line);
        if (logs.isEmpty()) {
            System.out.println("No logs found.");
        }
        for (ActivityLog l : logs) {
            System.out.printf(fmt, l.id(), l.userId(), cut(l.actionType(), 18),
                    cut(l.description(), 35), l.timestamp());
        }
        System.out.println(line);
    }

    private String cut(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
