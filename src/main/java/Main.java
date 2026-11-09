
import dao.ActivityLogDao;
import dao.DatabaseInitializer;
import dao.UserDao;
import ui.Authentication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize database
        DatabaseInitializer.initialize();

        // 2. Instantiate shared DAOs & utilities (one instance each)
        UserDao userDao = new UserDao();
        ActivityLogDao logDao = new ActivityLogDao();
        Scanner scanner = new Scanner(System.in);

        // 3. Delegate to Authentication
        new Authentication(scanner, userDao, logDao).start();

        scanner.close();
    }
}
