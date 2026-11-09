package ui;

import dao.ActivityLogDao;
import dao.UserDao;
import model.Roles;
import model.User;

import java.util.Scanner;

public class Authentication {
    private final Scanner scanner;
    private final UserDao userDao;
    private final ActivityLogDao logDao;

    public Authentication(Scanner scanner, UserDao userDao, ActivityLogDao logDao) {
        this.scanner = scanner;
        this.userDao = userDao;
        this.logDao = logDao;
    }

    public void start() {
        while (true) {
            System.out.println("\n===== MEDICINE RESERVATION AND PICKUP SYSTEM =====");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Username (0 to cancel): ");
        String username = scanner.nextLine().trim();
        if (username.equals("0")) return;
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

        String role = chooseRole();
        if (role == null) return;

        int id = userDao.registerUser(username, password, role);
        if (id > 0) {
            System.out.println("Registration successful! Your account is PENDING admin approval.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private String chooseRole() {
        while (true) {
            System.out.println("Select role:");
            for (int i = 0; i < Roles.REGISTRABLE.size(); i++) {
                System.out.println((i + 1) + ". " + Roles.REGISTRABLE.get(i));
            }
            System.out.println("0. Cancel");
            System.out.print("Choose: ");
            String in = scanner.nextLine().trim();
            if (in.equals("0")) return null;
            try {
                int n = Integer.parseInt(in);
                if (n >= 1 && n <= Roles.REGISTRABLE.size()) {
                    return Roles.REGISTRABLE.get(n - 1);
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid role choice.");
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userDao.login(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }
        if (!"APPROVED".equalsIgnoreCase(user.status())) {
            System.out.println("Your account is " + user.status() + ". Please wait for admin approval.");
            return;
        }
        System.out.println("Welcome, " + user.username() + " (" + user.role() + ")!");
        route(user);
        System.out.println("You have been logged out.");
    }

    private void route(User user) {
        switch (user.role().toUpperCase()) {
            case Roles.ADMIN -> new AdminMenu(scanner, userDao, logDao, user).show();
            case Roles.PHARMACIST -> new PharmacistMenu(scanner, logDao, user).show();
            case Roles.CUSTOMER -> new CustomerMenu(scanner, logDao, user).show();
            default -> System.out.println("No menu available for role: " + user.role());
        }
    }
}
