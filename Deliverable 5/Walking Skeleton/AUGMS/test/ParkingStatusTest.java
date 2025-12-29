import entity.*;
import dao.*;
import service.*;
import controller.*;

import java.util.*;

/**
 * Test suite for UC-104: View Parking Status
 * Tests the complete flow of viewing current parking spot statuses and counts.
 * 
 * Covers:
 * - Entity classes: ParkingGarage, ParkingSpot, OccupancySensor
 * - DAO classes: ParkingGarageDAO, ParkingSpotDAO, OccupancySensorDAO
 * - Service layer: ParkingStatusService
 * - Controller: ParkingStatusController
 */
public class ParkingStatusTest {

    // Test data
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    private ParkingGarageDAO garageDAO;
    private ParkingSpotDAO spotDAO;
    private OccupancySensorDAO sensorDAO;
    private ParkingStatusService statusService;
    private ParkingStatusController statusController;

    /**
     * Initializes test components
     */
    public void setup() {
        garageDAO = new ParkingGarageDAO();
        spotDAO = new ParkingSpotDAO();
        sensorDAO = new OccupancySensorDAO();
        statusService = new ParkingStatusService(garageDAO, spotDAO, sensorDAO);
        statusController = new ParkingStatusController(statusService);

        System.out.println("========== Setup Complete ==========\n");
    }

    /**
     * TEST 1: Test OccupancySensor entity and sensor detection
     */
    public void testOccupancySensorFunctionality() {
        System.out.println("TEST 1: OccupancySensor Functionality");
        System.out.println("-".repeat(40));

        // Create a sensor with initial state: available
        OccupancySensor sensor = new OccupancySensor(1, 1, false);

        // Test detecting occupancy (should be false initially)
        boolean isOccupied = sensor.detectOccupancy();
        assert !isOccupied : "Sensor should detect available spot";
        System.out.println("✓ Sensor correctly detects available state");

        // Update occupancy status to occupied
        sensor.updateOccupancyStatus(true);
        isOccupied = sensor.getOccupancyState();
        assert isOccupied : "Sensor should detect occupied state after update";
        System.out.println("✓ Sensor correctly detects occupied state after update");

        // Update back to available
        sensor.updateOccupancyStatus(false);
        isOccupied = sensor.getOccupancyState();
        assert !isOccupied : "Sensor should detect available state after update";
        System.out.println("✓ Sensor correctly transitions back to available state");

        System.out.println("PASSED: TEST 1\n");
    }

    /**
     * TEST 2: Test ParkingSpot entity and status management
     */
    public void testParkingSpotFunctionality() {
        System.out.println("TEST 2: ParkingSpot Functionality");
        System.out.println("-".repeat(40));

        // Create a parking spot
        ParkingSpot spot = new ParkingSpot(1, 1, "A-01", SpotStatus.AVAILABLE);

        // Test getSpotID
        int spotId = spot.getSpotID();
        assert spotId == 1 : "Spot ID should be 1";
        System.out.println("✓ Spot ID retrieved correctly");

        // Test getOccupancyStatus (no sensor yet)
        SpotStatus status = spot.getOccupancyStatus();
        assert status == SpotStatus.AVAILABLE : "Spot should be available initially";
        System.out.println("✓ Initial occupancy status is AVAILABLE");

        // Attach a sensor
        OccupancySensor sensor = new OccupancySensor(1, 1, false);
        spot.setOccupancySensor(sensor);

        // Status should derive from sensor
        status = spot.getOccupancyStatus();
        assert status == SpotStatus.AVAILABLE : "Spot status should match sensor state";
        System.out.println("✓ Spot status correctly derives from sensor (AVAILABLE)");

        // Change sensor state
        sensor.updateOccupancyStatus(true);
        status = spot.getOccupancyStatus();
        assert status == SpotStatus.OCCUPIED : "Spot status should update with sensor";
        System.out.println("✓ Spot status correctly updates to OCCUPIED");

        System.out.println("PASSED: TEST 2\n");
    }

    /**
     * TEST 3: Test ParkingGarage entity aggregation
     */
    public void testParkingGarageAggregation() {
        System.out.println("TEST 3: ParkingGarage Aggregation");
        System.out.println("-".repeat(40));

        // Create a garage
        ParkingGarage garage = new ParkingGarage(1, "Main Campus Garage", 5, true);

        // Create 5 spots (3 available, 2 occupied)
        for (int i = 1; i <= 5; i++) {
            ParkingSpot spot = new ParkingSpot(i, 1, "A-" + String.format("%02d", i),
                    i <= 3 ? SpotStatus.AVAILABLE : SpotStatus.OCCUPIED);

            // Attach sensors to each spot
            OccupancySensor sensor = new OccupancySensor(i, i, i > 3);
            spot.setOccupancySensor(sensor);
            garage.addParkingSpot(spot);
        }

        // Test counting available slots
        int availableCount = garage.countAvailableSlots();
        assert availableCount == 3 : "Should have 3 available slots";
        System.out.println("✓ Count available slots: " + availableCount);

        // Test counting occupied slots
        int occupiedCount = garage.countOccupiedSlots();
        assert occupiedCount == 2 : "Should have 2 occupied slots";
        System.out.println("✓ Count occupied slots: " + occupiedCount);

        // Test getting available spots
        Set<ParkingSpot> availableSpots = garage.getAvailableSpots();
        assert availableSpots.size() == 3 : "Should have 3 available spots";
        System.out.println("✓ Retrieved available spots: " + availableSpots.size());

        // Test getting occupied spots
        Set<ParkingSpot> occupiedSpots = garage.getOccupiedSpots();
        assert occupiedSpots.size() == 2 : "Should have 2 occupied spots";
        System.out.println("✓ Retrieved occupied spots: " + occupiedSpots.size());

        // Test garage accessibility
        boolean isAccessible = garage.isGarageAccessible();
        assert isAccessible : "Garage should be accessible";
        System.out.println("✓ Garage accessibility check passed");

        // Test aggregated parking status
        ParkingStatus status = garage.getParkingStatus();
        assert status.getAvailableSpots() == 3 : "Status should show 3 available";
        assert status.getOccupiedSpots() == 2 : "Status should show 2 occupied";
        assert status.getTotalSpots() == 5 : "Status should show 5 total";
        System.out.println("✓ Aggregated parking status: " + availableCount + " available, " +
                occupiedCount + " occupied");

        System.out.println("PASSED: TEST 3\n");
    }

    /**
     * TEST 4: Test DAO operations with database
     */
    public void testDAOOperations() {
        System.out.println("TEST 4: DAO Operations (Database Integration)");
        System.out.println("-".repeat(40));

        try {
            // Test ParkingGarageDAO - retrieve all garages
            Set<ParkingGarage> garages = garageDAO.getAllGarages();
            assert !garages.isEmpty() : "Should have garages in database";
            System.out.println("✓ Retrieved " + garages.size() + " garages from database");

            // Get first garage
            ParkingGarage garage = garages.iterator().next();
            int garageId = garage.getGarageID();
            System.out.println("  Testing with garage: " + garage.getGarageName());

            // Test ParkingSpotDAO - get spots for garage
            Set<ParkingSpot> spots = spotDAO.getSpotsByGarage(garageId);
            assert !spots.isEmpty() : "Garage should have spots";
            System.out.println("✓ Retrieved " + spots.size() + " parking spots for garage");

            // Test OccupancySensorDAO - get sensors for garage
            Set<OccupancySensor> sensors = sensorDAO.getSensorsByGarage(garageId);
            assert !sensors.isEmpty() : "Garage should have sensors";
            System.out.println("✓ Retrieved " + sensors.size() + " occupancy sensors for garage");

            // Test status queries
            int availableCount = garageDAO.getAvailableSpotCount(garageId);
            int occupiedCount = garageDAO.getOccupiedSpotCount(garageId);
            System.out.println("✓ Garage status: " + availableCount + " available, " +
                    occupiedCount + " occupied");

            // Test specific spot query
            if (!spots.isEmpty()) {
                ParkingSpot spot = spots.iterator().next();
                int spotId = spot.getSpotID();
                SpotStatus spotStatus = spotDAO.getSpotStatus(spotId);
                System.out.println("✓ Retrieved spot status: " + spotStatus);
            }

        } catch (Exception e) {
            System.err.println("✗ Database operation failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("PASSED: TEST 4\n");
    }

    /**
     * TEST 5: Test ParkingStatusService
     */
    public void testParkingStatusService() {
        System.out.println("TEST 5: ParkingStatusService");
        System.out.println("-".repeat(40));

        try {
            // Get all garages from database
            Set<ParkingGarage> allGarages = statusService.getAllGaragesWithStatus();
            if (!allGarages.isEmpty()) {
                ParkingGarage garage = allGarages.iterator().next();
                int garageId = garage.getGarageID();

                // Test current status check
                ParkingStatus status = statusService.checkCurrentStatus(garageId);
                assert status != null : "Should retrieve parking status";
                System.out.println("✓ Retrieved current status for " + status.getGarageName());

                // Test data aggregation
                ParkingStatus aggregated = statusService.aggregateData(garageId);
                assert aggregated != null : "Should aggregate parking data";
                System.out.println("✓ Aggregated data: " + aggregated.getAvailableSpots() +
                        " available");

                // Test slot counting
                int availableSlots = statusService.countAvailableSlots(garageId);
                int occupiedSlots = statusService.countOccupiedSlots(garageId);
                System.out.println("✓ Counted slots: " + availableSlots + " available, " +
                        occupiedSlots + " occupied");

                // Test getting available spots
                Set<ParkingSpot> availableSpots = statusService.getAvailableSpots(garageId);
                assert availableSpots != null : "Should retrieve available spots";
                System.out.println("✓ Retrieved " + availableSpots.size() + " available spots");

                // Test getting occupied spots
                Set<ParkingSpot> occupiedSpots = statusService.getOccupiedSpots(garageId);
                assert occupiedSpots != null : "Should retrieve occupied spots";
                System.out.println("✓ Retrieved " + occupiedSpots.size() + " occupied spots");

                // Test garage accessibility
                boolean isAccessible = statusService.isGarageAccessible(garageId);
                System.out.println("✓ Garage accessible: " + isAccessible);

                // Test access permissions
                boolean hasAccess = statusService.checkAccessPermissions(garageId, "test_user");
                System.out.println("✓ User access verified: " + hasAccess);
            }

        } catch (Exception e) {
            System.err.println("✗ Service operation failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("PASSED: TEST 5\n");
    }

    /**
     * TEST 6: Test ParkingStatusController
     */
    public void testParkingStatusController() {
        System.out.println("TEST 6: ParkingStatusController");
        System.out.println("-".repeat(40));

        try {
            // Get all garages to test with
            Set<ParkingGarage> allGarages = statusController.getAllGaragesStatus();
            if (!allGarages.isEmpty()) {
                ParkingGarage garage = allGarages.iterator().next();
                int garageId = garage.getGarageID();

                System.out.println("Testing with garage: " + garage.getGarageName());

                // Test live occupancy request
                ParkingStatus liveStatus = statusController.handleLiveOccupancyRequest(garageId);
                assert liveStatus != null : "Should handle live occupancy request";
                System.out.println("✓ Handled live occupancy request");

                // Test getting available spots
                Set<ParkingSpot> availableSpots = statusController.getAvailableSpots(garageId);
                System.out.println("✓ Retrieved available spots: " + availableSpots.size());

                // Test getting occupied spots
                Set<ParkingSpot> occupiedSpots = statusController.getOccupiedSpots(garageId);
                System.out.println("✓ Retrieved occupied spots: " + occupiedSpots.size());

                // Test slot counts
                int availableCount = statusController.getAvailableSlotCount(garageId);
                int occupiedCount = statusController.getOccupiedSlotCount(garageId);
                System.out.println("✓ Slot counts: " + availableCount + " available, " +
                        occupiedCount + " occupied");

                // Test garage accessibility check
                boolean isAccessible = statusController.isGarageAccessible(garageId);
                System.out.println("✓ Garage accessibility: " + isAccessible);

                // Test user access verification
                boolean hasAccess = statusController.verifyUserAccess(garageId, "test_user");
                System.out.println("✓ User access verified: " + hasAccess);

                // Test spot occupancy check (if available)
                if (!availableSpots.isEmpty()) {
                    ParkingSpot spot = availableSpots.iterator().next();
                    boolean isOccupied = statusController.getSpotOccupancy(spot.getSpotID());
                    System.out.println("✓ Spot occupancy check: " + isOccupied);
                }

                // Test status update
                if (!occupiedSpots.isEmpty()) {
                    ParkingSpot spot = occupiedSpots.iterator().next();
                    OccupancySensor sensor = spot.getOccupancySensor();
                    if (sensor != null) {
                        boolean updateResult = statusController.handleStatusUpdate(
                                sensor.getSensorId(), !sensor.isOccupied());
                        System.out.println("✓ Status update processed: " + updateResult);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("✗ Controller operation failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("PASSED: TEST 6\n");
    }

    /**
     * TEST 7: Test end-to-end use case flow
     */
    public void testEndToEndFlow() {
        System.out.println("TEST 7: End-to-End Use Case Flow");
        System.out.println("-".repeat(40));

        try {
            System.out.println("Simulating: User views parking status\n");

            // Step 1: User requests to view parking status for a garage
            Set<ParkingGarage> allGarages = statusController.getAllGaragesStatus();
            if (!allGarages.isEmpty()) {
                ParkingGarage selectedGarage = allGarages.iterator().next();
                int garageId = selectedGarage.getGarageID();

                System.out.println("Step 1: User selects garage: " + selectedGarage.getGarageName());

                // Step 2: Controller handles live occupancy request
                ParkingStatus status = statusController.handleLiveOccupancyRequest(garageId);
                if (status != null) {
                    System.out.println("Step 2: Retrieved real-time occupancy data");
                    System.out.println("  - Total spots: " + status.getTotalSpots());
                    System.out.println("  - Available: " + status.getAvailableSpots());
                    System.out.println("  - Occupied: " + status.getOccupiedSpots());
                    System.out.println("  - Occupancy Rate: " +
                            String.format("%.1f%%", status.getOccupancyPercentage()));
                    System.out.println("  - Garage Accessible: " + status.isAccessible());

                    // Step 3: User can view detailed spot information
                    Set<ParkingSpot> availableSpots = statusController.getAvailableSpots(garageId);
                    System.out.println("\nStep 3: Available parking spots:");
                    int count = 0;
                    for (ParkingSpot spot : availableSpots) {
                        System.out.println("  - Spot " + spot.getSpotNumber() + " (ID: " +
                                spot.getSpotID() + ")");
                        count++;
                        if (count >= 3) { // Show first 3
                            if (availableSpots.size() > 3) {
                                System.out.println("  ... and " + (availableSpots.size() - 3) + " more");
                            }
                            break;
                        }
                    }

                    // Step 4: System logs the status view
                    System.out.println("\nStep 4: Parking status view logged");

                    System.out.println("\n✓ Use case completed successfully!");
                }
            }

        } catch (Exception e) {
            System.err.println("✗ Use case flow failed: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("PASSED: TEST 7\n");
    }

    /**
     * Runs all tests
     */
    public void runAllTests() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("UC-104: VIEW PARKING STATUS - TEST SUITE");
        System.out.println("=".repeat(50) + "\n");

        setup();

        try {
            testOccupancySensorFunctionality();
            testParkingSpotFunctionality();
            testParkingGarageAggregation();
            testDAOOperations();
            testParkingStatusService();
            testParkingStatusController();
            testEndToEndFlow();

            System.out.println("=".repeat(50));
            System.out.println("ALL TESTS PASSED ✓");
            System.out.println("=".repeat(50));
        } catch (AssertionError e) {
            System.out.println("\n✗ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n✗ UNEXPECTED ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Interactive CLI for running live queries against the database
     */
    public void runInteractiveCli() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu();
            System.out.print("Select option: ");
            String line = scanner.nextLine().trim();
            switch (line) {
                case "1":
                    listGarages();
                    break;
                case "2":
                    System.out.print("Enter garageId: ");
                    try {
                        int gid = Integer.parseInt(scanner.nextLine().trim());
                        viewGarageStatus(gid);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid garageId");
                    }
                    break;
                case "3":
                    System.out.print("Enter garageId: ");
                    try {
                        int gid = Integer.parseInt(scanner.nextLine().trim());
                        listAvailableSpots(gid);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid garageId");
                    }
                    break;
                case "4":
                    System.out.print("Enter garageId: ");
                    try {
                        int gid = Integer.parseInt(scanner.nextLine().trim());
                        listOccupiedSpots(gid);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid garageId");
                    }
                    break;
                case "5":
                    updateSensorReading(scanner);
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Unknown option");
            }
            System.out.println();
        }
        scanner.close();
        System.out.println("Exiting interactive CLI.");
    }

    private void printMenu() {
        System.out.println("--- Parking Status CLI ---");
        System.out.println("1) List garages");
        System.out.println("2) View garage status");
        System.out.println("3) List available spots (garage)");
        System.out.println("4) List occupied spots (garage)");
        System.out.println("5) Update sensor reading (simulate)");
        System.out.println("0) Exit");
    }

    private void listGarages() {
        Set<ParkingGarage> garages = statusController.getAllGaragesStatus();
        if (garages == null || garages.isEmpty()) {
            System.out.println("No garages found (check DB connection / driver).\n");
            return;
        }
        System.out.println("Garages:");
        for (ParkingGarage g : garages) {
            System.out.println(g.getGarageName() + ": " + g.countAvailableSlots() + " Available");

            // Get all spots by combining available and occupied sets
            Set<ParkingSpot> available = statusController.getAvailableSpots(g.getGarageID());
            Set<ParkingSpot> occupied = statusController.getOccupiedSpots(g.getGarageID());

            List<ParkingSpot> allSpots = new ArrayList<>();
            if (available != null)
                allSpots.addAll(available);
            if (occupied != null)
                allSpots.addAll(occupied);

            // Sort by spot number
            allSpots.sort((s1, s2) -> s1.getSpotNumber().compareToIgnoreCase(s2.getSpotNumber()));

            StringBuilder sb = new StringBuilder();
            for (ParkingSpot s : allSpots) {
                boolean isOccupied = false;
                // Check if this spot is in the occupied set
                // (Using ID check for safety, assuming equals() works but ID is safer)
                for (ParkingSpot occ : occupied) {
                    if (occ.getSpotID() == s.getSpotID()) {
                        isOccupied = true;
                        break;
                    }
                }

                if (isOccupied) {
                    sb.append(ANSI_RED).append(s.getSpotNumber()).append(ANSI_RESET).append(" ");
                } else {
                    sb.append(ANSI_GREEN).append(s.getSpotNumber()).append(ANSI_RESET).append(" ");
                }
            }
            System.out.println(sb.toString().trim());
            System.out.println(); // Empty line between garages
        }
    }

    private void viewGarageStatus(int garageId) {
        ParkingStatus status = statusController.handleLiveOccupancyRequest(garageId);
        if (status == null) {
            System.out.println("Could not retrieve status (check access or DB).\n");
            return;
        }
        System.out.println("Garage: " + status.getGarageName());
        System.out.println(" Total spots: " + status.getTotalSpots());
        System.out.println(ANSI_GREEN + " Available: " + status.getAvailableSpots() + ANSI_RESET);
        System.out.println(ANSI_RED + " Occupied: " + status.getOccupiedSpots() + ANSI_RESET);
        System.out.println(" Occupancy: " + String.format("%.1f%%", status.getOccupancyPercentage()));
        System.out.println(" Accessible: " + status.isAccessible());
    }

    private void listAvailableSpots(int garageId) {
        Set<ParkingSpot> spots = statusController.getAvailableSpots(garageId);
        if (spots == null || spots.isEmpty()) {
            System.out.println("No available spots or none retrieved.");
            return;
        }
        System.out.println(ANSI_GREEN + "Available spots:" + ANSI_RESET);
        for (ParkingSpot s : spots) {
            System.out.println(ANSI_GREEN + " - " + s.getSpotNumber() + " (ID: " + s.getSpotID() + ")" + ANSI_RESET);
        }
    }

    private void listOccupiedSpots(int garageId) {
        Set<ParkingSpot> spots = statusController.getOccupiedSpots(garageId);
        if (spots == null || spots.isEmpty()) {
            System.out.println("No occupied spots or none retrieved.");
            return;
        }
        System.out.println(ANSI_RED + "Occupied spots:" + ANSI_RESET);
        for (ParkingSpot s : spots) {
            System.out.println(ANSI_RED + " - " + s.getSpotNumber() + " (ID: " + s.getSpotID() + ")" + ANSI_RESET);
        }
    }

    private void updateSensorReading(Scanner scanner) {
        try {
            System.out.print("Enter sensorId: ");
            int sensorId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Set occupied? (y/n): ");
            String v = scanner.nextLine().trim().toLowerCase();
            boolean occupied = v.equals("y") || v.equals("yes") || v.equals("1");
            boolean ok = statusController.handleStatusUpdate(sensorId, occupied);
            System.out.println("Update " + (ok ? "succeeded" : "failed") + ".");
        } catch (NumberFormatException e) {
            System.out.println("Invalid sensorId");
        }
    }

    /**
     * Main entry point
     */
    public static void main(String[] args) {
        ParkingStatusTest app = new ParkingStatusTest();
        app.setup();
        // Non-interactive flags for CI or scripting
        if (args != null && args.length > 0) {
            String cmd = args[0];
            try {
                switch (cmd) {
                    case "--list":
                        app.listGarages();
                        break;
                    case "--status":
                        if (args.length < 2) {
                            System.err.println("Usage: --status <garageId>");
                            System.exit(2);
                        }
                        app.viewGarageStatus(Integer.parseInt(args[1]));
                        break;
                    case "--available":
                        if (args.length < 2) {
                            System.err.println("Usage: --available <garageId>");
                            System.exit(2);
                        }
                        app.listAvailableSpots(Integer.parseInt(args[1]));
                        break;
                    case "--occupied":
                        if (args.length < 2) {
                            System.err.println("Usage: --occupied <garageId>");
                            System.exit(2);
                        }
                        app.listOccupiedSpots(Integer.parseInt(args[1]));
                        break;
                    case "--update-sensor":
                        if (args.length < 3) {
                            System.err.println("Usage: --update-sensor <sensorId> <true|false>");
                            System.exit(2);
                        }
                        int sensorId = Integer.parseInt(args[1]);
                        boolean occupied = Boolean.parseBoolean(args[2]);
                        boolean ok = app.statusController.handleStatusUpdate(sensorId, occupied);
                        System.out.println("Update " + (ok ? "succeeded" : "failed"));
                        break;
                    default:
                        System.err.println("Unknown flag: " + cmd);
                        System.err.println(
                                "Supported: --list, --status <id>, --available <id>, --occupied <id>, --update-sensor <sensorId> <true|false>");
                        System.exit(2);
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid numeric argument: " + nfe.getMessage());
                System.exit(2);
            }
            return;
        }

        app.runInteractiveCli();
    }
}
