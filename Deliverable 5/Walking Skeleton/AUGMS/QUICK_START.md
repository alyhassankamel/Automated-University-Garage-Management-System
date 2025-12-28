# Quick Start Guide - AUGMS JavaFX Application

## Prerequisites Check
- [ ] Java 11 or higher installed (`java -version`)
- [ ] Maven 3.6+ installed (`mvn -version`)
- [ ] SQL Server running and accessible
- [ ] Database `AUGMS` created

## Step 1: Database Setup (5 minutes)

1. **Create the database** (if not exists):
   ```sql
   CREATE DATABASE AUGMS;
   GO
   ```

2. **Run the schema script**:
   - Open SQL Server Management Studio
   - Connect to your SQL Server instance
   - Open `database_schema.sql`
   - Execute the script (F5)
   - Verify: You should see 20 parking spots created (SPOT001-SPOT020)

3. **Update connection settings**:
   - Open `AUGMS/util/DatabaseConnection.java`
   - Update these lines:
     ```java
     private static final String DB_USER = "sa";  // Your SQL Server username
     private static final String DB_PASSWORD = "your_password_here";  // Your password
     ```
   - If using a different server/port, update `DB_URL` as well

## Step 2: Build the Project (2 minutes)

Open terminal/command prompt in the project directory:

```bash
cd "C:\xampp\htdocs\StarUML to Java V1"
mvn clean compile
```

Expected output: `BUILD SUCCESS`

## Step 3: Run the Application (1 minute)

```bash
mvn javafx:run
```

The main menu window should appear!

## Step 4: Test the Application

### Test Check-In:
1. Click "Vehicle Check-In"
2. Enter license plate: `ABC123`
3. Select vehicle type: `Car`
4. Enter owner name: `John Doe` (optional)
5. Click "Check In"
6. **Expected**: Success message with assigned slot (e.g., SPOT001)

### Test Check-Out:
1. Click "Vehicle Check-Out"
2. Enter license plate: `ABC123`
3. Click "Check Out"
4. **Expected**: Confirmation with duration and fee

### Test View Slots:
1. Click "View Available Slots"
2. **Expected**: Table showing all 20 slots
3. Check-in slot should show as "Occupied" with vehicle plate
4. Click "Refresh" to update

## Troubleshooting

### "Cannot connect to database"
- Verify SQL Server is running
- Check connection string in `DatabaseConnection.java`
- Test connection with SQL Server Management Studio
- Ensure SQL Server authentication is enabled

### "FXML file not found"
- Ensure FXML files are in `AUGMS/fxml/` directory
- Check resource paths in controllers

### "JavaFX runtime components are required"
- Run `mvn clean install` to download dependencies
- Verify JavaFX dependencies in `pom.xml`

### "No available parking slots"
- Check database: `SELECT * FROM ParkingSpots WHERE status = 'AVAILABLE'`
- Run schema script again if needed

## Database Verification Queries

```sql
-- Check vehicles
SELECT * FROM Vehicles;

-- Check parking spots
SELECT * FROM ParkingSpots;

-- Check entry/exit logs
SELECT * FROM EntryExitLogs ORDER BY timestamp DESC;

-- Check available spots
SELECT COUNT(*) as AvailableSpots 
FROM ParkingSpots 
WHERE status = 'AVAILABLE';
```

## Next Steps

- Customize the UI colors in `AUGMS/css/styles.css`
- Add more parking spots in the database
- Modify fee calculation in `CheckOutController.java`
- Add additional validation rules

## Support

Refer to:
- `README.md` for detailed documentation
- `IMPLEMENTATION_SUMMARY.md` for technical details
- `database_schema.sql` for database structure

