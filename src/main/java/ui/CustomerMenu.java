package ui;

import dao.ActivityLogDao;
import model.User;

import java.util.Scanner;

/** Placeholder: paste your existing customer/patient features here. */
public class CustomerMenu {
    private final Scanner scanner;
    private final ActivityLogDao logDao;
    private final User user;

    public CustomerMenu(Scanner scanner, ActivityLogDao logDao, User user) {
        this.scanner = scanner;
        this.logDao = logDao;
        this.user = user;
    }

    public void show() {
        while (true) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. Reserve Medicine (plug in your existing feature)");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            String c = scanner.nextLine().trim();
            if (c.equals("0")) return;
            if (c.equals("1")) {
                // Example of domain logging - call this where a reservation is really saved:
                // logDao.logAction(user.id(), "MEDICINE_RESERVED", "Customer [" + user.id() + "] reserved " + medicineName);
                System.out.println("Not implemented yet - connect your existing reservation code here.");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
