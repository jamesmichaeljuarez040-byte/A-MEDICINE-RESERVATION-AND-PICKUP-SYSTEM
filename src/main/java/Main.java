import dao.DatabaseInitializer;
import dao.UserDao;
import ui.Authentication;

import java.util.Scanner;

/**
 * Application entry point. Startup only:
 * Initialize Database -> Instantiate DAOs & Utilities -> Authentication.start()
 */
public class Main {
    public static void main(String[] args) {
        // 1. Initialize database
        DatabaseInitializer.initialize();

        // 2. Instantiate shared DAOs and utilities (one instance each)
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);

        // 3. Delegate to the authentication UI
        new Authentication(userDao, scanner).start();

        scanner.close();
    }
}
