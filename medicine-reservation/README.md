# Medicine Reservation and Pickup System

Layered Java + SQLite console app (Maven).

## Structure
- `dao/` - `DatabaseConnection`, `UserDao` (PreparedStatement queries)
- `model/` - `User` (Java record)
- `ui/` - `MainMenu`, `UserMenu`, `AdminMenu`
- `Main.java` - entry point

## Run
```
mvn compile exec:java
```
Requires JDK 17+ and Maven. The `medicine_reservation.db` file is created automatically.

## Admin features
Admins see **Manage Users** after login: view all users in a table and update a user's role and status (enter `0` to cancel at any step; the active admin account cannot be edited).
