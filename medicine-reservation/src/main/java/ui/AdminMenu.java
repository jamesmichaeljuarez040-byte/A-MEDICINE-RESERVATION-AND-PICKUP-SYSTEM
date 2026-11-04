package ui;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Admin screen: view all users and update a user's role and status.
 * Enter 0 at any prompt of the update flow to cancel.
 */
public class AdminMenu {

    private static final Set<String> VALID_ROLES = Set.of("CUSTOMER", "PHARMACIST", "ADMIN");
    private static final Set<String> VALID_STATUSES = Set.of("APPROVED", "PENDING");
    private static final String CANCEL = "0";

    private final User currentAdmin;
    private final Scanner scanner;
    private final UserDao userDao;

    public AdminMenu(User currentAdmin, Scanner scanner, UserDao userDao) {
        this.currentAdmin = currentAdmin;
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("[1] Update User Role/Status");
            System.out.println("[2] Back");
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> updateUser();
                case "2" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void updateUser() {
        List<User> users = userDao.getAllUsers();
        printUserTable(users);

        int targetUserId = readIntInput("Enter User ID to update (0 to cancel): ");
        if (targetUserId == 0) {
            System.out.println("Update cancelled.");
            return;
        }
        if (targetUserId == currentAdmin.id()) {
            System.out.println("WARNING: Modifying the currently active admin account is restricted.");
            return;
        }
        boolean exists = users.stream().anyMatch(u -> u.id() == targetUserId);
        if (!exists) {
            System.out.println("User ID not found.");
            return;
        }

        String newRole = readValidRole();
        if (newRole == null) {
            System.out.println("Update cancelled.");
            return;
        }
        String newStatus = readValidStatus();
        if (newStatus == null) {
            System.out.println("Update cancelled.");
            return;
        }

        if (userDao.updateUserRoleAndStatus(targetUserId, newRole, newStatus)) {
            System.out.println("User " + targetUserId + " updated: Role=" + newRole + ", Status=" + newStatus);
        } else {
            System.out.println("Update failed. Please try again.");
        }
    }

    private void printUserTable(List<User> users) {
        String line = "+------+----------------------+------------+----------+";
        System.out.println("\n" + line);
        System.out.printf("| %-4s | %-20s | %-10s | %-8s |%n", "ID", "Username", "Role", "Status");
        System.out.println(line);
        for (User u : users) {
            System.out.printf("| %-4d | %-20s | %-10s | %-8s |%n",
                    u.id(), u.username(), u.roles(), u.status());
        }
        System.out.println(line);
    }

    // ---------- Input validation wrappers ----------

    /** Keeps asking until the user enters a whole number. Never throws on bad input. */
    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 0; // input closed: treat as cancel
            }
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /** Returns a valid role in upper case, or null if the user entered 0 to cancel. */
    private String readValidRole() {
        while (true) {
            System.out.print("Enter new role [CUSTOMER, PHARMACIST, ADMIN] (0 to cancel): ");
            if (!scanner.hasNextLine()) {
                return null;
            }
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals(CANCEL)) {
                return null;
            }
            if (VALID_ROLES.contains(input)) {
                return input;
            }
            System.out.println("Invalid role. Choose CUSTOMER, PHARMACIST, or ADMIN.");
        }
    }

    /** Returns a valid status in upper case, or null if the user entered 0 to cancel. */
    private String readValidStatus() {
        while (true) {
            System.out.print("Enter new status [APPROVED, PENDING] (0 to cancel): ");
            if (!scanner.hasNextLine()) {
                return null;
            }
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals(CANCEL)) {
                return null;
            }
            if (VALID_STATUSES.contains(input)) {
                return input;
            }
            System.out.println("Invalid status. Choose APPROVED or PENDING.");
        }
    }
}
