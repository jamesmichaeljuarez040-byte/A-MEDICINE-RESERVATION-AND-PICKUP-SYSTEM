package ui;

import dao.ActivityLogDao;
import model.User;

import java.util.Scanner;

/** Placeholder: paste your existing pharmacist features here. */
public class PharmacistMenu {
    private final Scanner scanner;
    private final ActivityLogDao logDao;
    private final User user;

    public PharmacistMenu(Scanner scanner, ActivityLogDao logDao, User user) {
        this.scanner = scanner;
        this.logDao = logDao;
        this.user = user;
    }

    public void show() {
        while (true) {
            System.out.println("\n===== PHARMACIST MENU =====");
            System.out.println("1. Process Pickup (plug in your existing feature)");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            String c = scanner.nextLine().trim();
            if (c.equals("0")) return;
            if (c.equals("1")) {
                // Example of domain logging - call this where a pickup is really completed:
                // logDao.logAction(user.id(), "PICKUP_COMPLETED", "Pharmacist [" + user.id() + "] released reservation #" + reservationId);
                System.out.println("Not implemented yet - connect your existing pickup code here.");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
