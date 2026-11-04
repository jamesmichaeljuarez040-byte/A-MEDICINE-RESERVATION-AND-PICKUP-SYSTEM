package ui;

import dao.UserDao;
import model.User;

import java.util.Scanner;

/**
 * Screen shown after a successful login. Logging out ends the session.
 * Admins also get access to user management.
 */
public class UserMenu {

    private final User user;
    private final Scanner scanner;
    private final UserDao userDao;

    public UserMenu(User user, Scanner scanner, UserDao userDao) {
        this.user = user;
        this.scanner = scanner;
        this.userDao = userDao;
    }

    public void show() {
        System.out.println("\nLogin successful!");
        boolean isAdmin = "ADMIN".equals(user.roles());
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- WELCOME, " + user.username() + " ---");
            System.out.println("Role   : " + user.roles());
            System.out.println("Status : " + user.status());
            if (isAdmin) {
                System.out.println("[1] Manage Users (Update Role/Status)");
                System.out.println("[2] Logout");
            } else {
                System.out.println("[1] Logout");
            }
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            String choice = scanner.nextLine().trim();
            if (isAdmin && choice.equals("1")) {
                new AdminMenu(user, scanner, userDao).show();
            } else if ((isAdmin && choice.equals("2")) || (!isAdmin && choice.equals("1"))) {
                System.out.println("You have been logged out.");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
