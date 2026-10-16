package ui;

import model.User;

import java.util.Scanner;

/**
 * Screen shown after a successful login. Logging out ends the session.
 */
public class UserMenu {

    private final User user;
    private final Scanner scanner;

    public UserMenu(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    public void show() {
        System.out.println("\nLogin successful!");
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- WELCOME, " + user.username() + " ---");
            System.out.println("Role   : " + user.roles());
            System.out.println("Status : " + user.status());
            System.out.println("[1] Logout");
            System.out.print("Choose: ");

            if (scanner.nextLine().trim().equals("1")) {
                System.out.println("You have been logged out.");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
