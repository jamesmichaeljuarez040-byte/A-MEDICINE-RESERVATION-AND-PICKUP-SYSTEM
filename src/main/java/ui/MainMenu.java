package ui;

import dao.UserDao;
import model.User;

import java.util.Optional;
import java.util.Scanner;

/**
 * Main menu: Register, Login, Exit.
 */
public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

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

        String role = null;
        while (role == null) {
            System.out.println("Select role: [1] CUSTOMER  [2] PHARMACIST  [3] ADMIN");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> role = "CUSTOMER";
                case "2" -> role = "PHARMACIST";
                case "3" -> role = "ADMIN";
                default -> System.out.println("Invalid role choice.");
            }
        }

        if (userDao.register(username, password, role)) {
            System.out.println("Registration successful! Status: PENDING");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> user = userDao.login(username, password);
        if (user.isPresent()) {
            new UserMenu(user.get(), scanner).show();
        } else {
            System.out.println("Invalid username or password.");
        }
    }
}
