package ui;

import dao.UserDao;
import model.User;

import java.util.Optional;
import java.util.Scanner;

/**
 * Handles registration, login and role-based routing.
 * Receives the single shared UserDao and Scanner from Main and passes them on.
 */
public class Authentication {

    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n==========================================");
            System.out.println(" MEDICINE RESERVATION AND PICKUP SYSTEM");
            System.out.println("==========================================");
            System.out.println("[1] Register Account");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Thank you. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1-3.");
            }
        }
    }

    private void register() {
        System.out.println("\n--- REGISTER ACCOUNT ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (userDao.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        String role = selectRole();
        if (role == null) {
            return; // input ended
        }

        if (userDao.register(username, password, role)) {
            System.out.println("Registration successful! Role: " + role + " | Status: PENDING");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    /** Keeps asking until a valid role is chosen. Returns null if input ends. */
    private String selectRole() {
        while (true) {
            System.out.println("Select role: [1] CUSTOMER  [2] PHARMACIST  [3] ADMIN");
            System.out.print("Choose: ");
            if (!scanner.hasNextLine()) {
                return null;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> { return "CUSTOMER"; }
                case "2" -> { return "PHARMACIST"; }
                case "3" -> { return "ADMIN"; }
                default -> System.out.println("Invalid role choice.");
            }
        }
    }

    private void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> result = userDao.login(username, password);
        if (result.isEmpty()) {
            System.out.println("Invalid username or password.");
            return;
        }
        User user = result.get();
        switch (user.status()) {
<<<<<<< HEAD
            case "APPROVED" -> routeByRole(user);
            case "PENDING" -> System.out.println("Your account is still PENDING. Please wait for an admin to approve it.");
=======
            case "ACTIVE" -> routeByRole(user);
            case "PENDING" -> System.out.println("Your account is still PENDING. Please wait for an admin to approve it.");
            case "REJECTED" -> System.out.println("Your account registration was REJECTED by an admin.");
>>>>>>> c8a13d48b8ddebc616480820bba7e7146ee3a01b
            default -> System.out.println("Your account cannot log in (status: " + user.status() + ").");
        }
    }

    /** Sends the logged-in user straight to the menu for their role. */
    private void routeByRole(User user) {
        System.out.println("\nLogin successful!");
        switch (user.roles()) {
            case "CUSTOMER" -> new CustomerMenu(user, userDao, scanner).show();
            case "PHARMACIST" -> new PharmacistMenu(user, userDao, scanner).show();
            case "ADMIN" -> new AdminMenu(user, userDao, scanner).show();
            default -> System.out.println("Unknown role: " + user.roles());
        }
    }
}
