# Medicine Reservation and Pickup System

Layered Java + SQLite console app (Maven).

## Structure
- `dao/` - `DatabaseConnection`, `UserDao` (PreparedStatement queries)
- `model/` - `User` (Java record)
- `ui/` - `MainMenu`, `UserMenu`
- `Main.java` - entry point

## Run
```
mvn compile exec:java
```
Requires JDK 17+ and Maven. The `medicine_reservation.db` file is created automatically.
