package ui;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.Scanner;

/**
 * Admin menu. Uses the shared UserDao and Scanner passed in from Authentication.
 * The admin reviews PENDING accounts and approves or rejects them.
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
            System.out.println("[1] View Pending Accounts");
            System.out.println("[2] Approve Account");
            System.out.println("[3] Reject Account");
            System.out.println("[4] Logout");
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> showPending();
                case "2" -> changeStatus("ACTIVE", "approve");
                case "3" -> changeStatus("REJECTED", "reject");
                case "4" -> {
                    System.out.println("You have been logged out.");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1-4.");
            }
        }
    }

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

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        User target = pending.stream().filter(u -> u.id() == id).findFirst().orElse(null);
        if (target == null) {
            System.out.println("No pending account with that ID.");
            return;
        }

        if (userDao.updateStatus(id, newStatus)) {
            System.out.println("Account '" + target.username() + "' is now " + newStatus + ".");
        } else {
            System.out.println("Update failed. Please try again.");
        }
    }
}
