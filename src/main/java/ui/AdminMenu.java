package ui;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.Scanner;

/**
 * Admin menu. Uses the shared UserDao and Scanner passed in from Authentication.
 * The admin views all users, views pending registrations and approves accounts.
 */
public class AdminMenu {

    private final User user;
    private final UserDao userDao;
    private final Scanner scanner;

    public AdminMenu(User user, UserDao userDao, Scanner scanner) {
        this.user = user;
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("Welcome, " + user.username());
            System.out.println("[1] View All System Users");
            System.out.println("[2] View Pending Registrations");
            System.out.println("[3] Approve User Account");
            System.out.println("[4] Logout");
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> viewPendingUsers();
                case "3" -> approveUser();
                case "4" -> {
                    System.out.println("You have been logged out.");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1-4.");
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n--- ALL SYSTEM USERS ---");
        printUserTable(userDao.getAllUsers());
    }

    private List<User> viewPendingUsers() {
        List<User> pending = userDao.getPendingUsers();
        System.out.println("\n--- PENDING REGISTRATIONS ---");
        printUserTable(pending);
        return pending;
    }

    private void approveUser() {
        List<User> pending = viewPendingUsers();
        if (pending.isEmpty()) {
            return;
        }

        System.out.print("Enter Target User ID to approve (0 to cancel): ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String input = scanner.nextLine().trim();

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
            return;
        }

        if (id == 0) {
            System.out.println("Approval cancelled.");
            return;
        }

        User target = pending.stream().filter(u -> u.id() == id).findFirst().orElse(null);
        if (target == null) {
            System.out.println("No pending account with ID " + id + ".");
            return;
        }

        if (userDao.approveUser(id)) {
            System.out.println("Account '" + target.username() + "' has been APPROVED.");
        } else {
            System.out.println("Approval failed. Please try again.");
        }
    }

    /** Prints any list of users as an aligned table with borders. */
    private void printUserTable(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No accounts to display.");
            return;
        }
        String border = "+" + "-".repeat(7) + "+" + "-".repeat(17) + "+" + "-".repeat(14) + "+" + "-".repeat(12) + "+";
        String format = "| %-5s | %-15s | %-12s | %-10s |%n";

        System.out.println(border);
        System.out.printf(format, "ID", "USERNAME", "ROLE", "STATUS");
        System.out.println(border);
        for (User u : users) {
            System.out.printf(format, u.id(), fit(u.username(), 15), fit(u.roles(), 12), fit(u.status(), 10));
        }
        System.out.println(border);
        System.out.println("Total: " + users.size());
    }

    /** Cuts text that is too long so the columns stay aligned. */
    private String fit(String text, int width) {
        return text.length() <= width ? text : text.substring(0, width - 1) + "~";
    }
}
