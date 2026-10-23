package ui;

import dao.UserDao;
import model.User;

import java.util.Scanner;

/**
 * Pharmacist menu. Uses the shared UserDao and Scanner passed in from Authentication.
 * Role features (reservations, pickups, etc.) will be added here in later deliverables.
 */
public class PharmacistMenu {

    private final User user;
    private final UserDao userDao;
    private final Scanner scanner;

    public PharmacistMenu(User user, UserDao userDao, Scanner scanner) {
        this.user = user;
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- PHARMACIST MENU ---");
            System.out.println("Welcome, " + user.username());
            System.out.println("Role   : " + user.roles());
            System.out.println("Status : " + user.status());
            System.out.println("[1] Logout");
            System.out.print("Choose: ");

            if (!scanner.hasNextLine()) {
                return;
            }
            if (scanner.nextLine().trim().equals("1")) {
                System.out.println("You have been logged out.");
                loggedIn = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
