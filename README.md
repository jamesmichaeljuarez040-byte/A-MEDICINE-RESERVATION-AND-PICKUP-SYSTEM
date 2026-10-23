# Medicine Reservation and Pickup System

Layered Java + SQLite console app (Maven).

## Structure
- `Main.java` - startup only: DatabaseInitializer -> create UserDao + Scanner -> `Authentication.start()`
- `dao/` - `DatabaseConnection` (enables `PRAGMA foreign_keys = ON` on every connection), `DatabaseInitializer` (creates tables), `UserDao`
- `model/` - `User` (Java record)
- `ui/` - `Authentication` (register, login, role routing), `CustomerMenu`, `PharmacistMenu`, `AdminMenu`

## Run
```
mvn compile exec:java
```
Requires JDK 17+ and Maven. The `medicine_reservation.db` file is created automatically.
New accounts are saved with status `PENDING` and cannot log in until an admin approves them.

## Default admin
A first admin is created automatically on first run: username `admin`, password `admin123` (status `APPROVED`).
Log in as admin -> **View All Users / View Pending / Approve User** (enter `0` to cancel).
