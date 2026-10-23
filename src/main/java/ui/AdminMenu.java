package ui;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.Scanner;

/**
 * Admin menu. Uses the shared UserDao and Scanner passed in from Authentication.
<<<<<<< HEAD
 * The admin views all users, views pending registrations and approves accounts.
=======
 * The admin reviews PENDING accounts and approves or rejects them.
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
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
<<<<<<< HEAD
            System.out.println("[1] View All System Users");
            System.out.println("[2] View Pending Registrations");
            System.out.println("[3] Approve User Account");
=======
            System.out.println("[1] View Pending Accounts");
            System.out.println("[2] Approve Account");
            System.out.println("[3] Reject Account");
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
            System.out.println("[4] Logout");
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
<<<<<<< HEAD
                case "1" -> viewAllUsers();
                case "2" -> viewPendingUsers();
                case "3" -> approveUser();
=======
                case "1" -> showPending();
                case "2" -> changeStatus("ACTIVE", "approve");
                case "3" -> changeStatus("REJECTED", "reject");
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
                case "4" -> {
                    System.out.println("You have been logged out.");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1-4.");
            }
        }
    }

<<<<<<< HEAD
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
=======
    /** Prints the pending list and returns it. */
    private List<User> showPending() {
        List<User> pending = userDao.findByStatus("PENDING");
        System.out.println("\n--- PENDING ACCOUNTS ---");
        if (pending.isEmpty()) {
            System.out.println("No pending accounts.");
        } else {
            System.out.printf("%-5s %-20s %-12s%n", "ID", "USERNAME", "ROLE");
            for (User u : pending) {
                System.out.printf("%-5d %-20s %-12s%n", u.id(), u.username(), u.roles());
            }
        }
        return pending;
    }

    private void changeStatus(String newStatus, String action) {
        List<User> pending = showPending();
        if (pending.isEmpty()) {
            return;
        }
        System.out.print("Enter the ID of the account to " + action + " (or press Enter to cancel): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Cancelled.");
            return;
        }
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
<<<<<<< HEAD
            System.out.println("Invalid ID. Please enter a number.");
            return;
        }

        if (id == 0) {
            System.out.println("Approval cancelled.");
=======
            System.out.println("Invalid ID.");
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
            return;
        }

        User target = pending.stream().filter(u -> u.id() == id).findFirst().orElse(null);
        if (target == null) {
<<<<<<< HEAD
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
=======
            System.out.println("No pending account with that ID.");
            return;
        }

        if (userDao.updateStatus(id, newStatus)) {
            System.out.println("Account '" + target.username() + "' is now " + newStatus + ".");
        } else {
            System.out.println("Update failed. Please try again.");
        }
    }
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
}
