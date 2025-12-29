package service;

import dao.*;
import entity.*;
import java.sql.Timestamp;

/**
 * Domain System Controller for Garage Operations.
 */
public class GarageSystem {

    private VehicleDAO vehicleDAO;
    private ParkingSessionDAO parkingSessionDAO;
    private EntryExitLogDAO entryExitLogDAO;
    private OccupancySensorDAO occupancySensorDAO;
    private ParkingSpotDAO parkingSpotDAO;

    public GarageSystem() {
        this.vehicleDAO = new VehicleDAO();
        this.parkingSessionDAO = new ParkingSessionDAO();
        this.entryExitLogDAO = new EntryExitLogDAO();
        this.occupancySensorDAO = new OccupancySensorDAO();
        this.parkingSpotDAO = new ParkingSpotDAO();
    }

    /**
     * Processes a vehicle exit.
     * 
     * @param licensePlate The license plate of the exiting vehicle.
     * @param timestamp    The time of exit.
     * @param exitGateId   The ID of the exit gate.
     * @return String confirmation message or error.
     */
    public String processExit(String licensePlate, Timestamp timestamp, int exitGateId) {
        // 1. Validate Vehicle
        Vehicle vehicle = vehicleDAO.findVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            return "Error: Vehicle not found: " + licensePlate;
        }

        // 2. Get Active Session
        ParkingSession session = parkingSessionDAO.findActiveSession(vehicle.getVehicleId());
        if (session == null) {
            return "Error: No active parking session found for vehicle: " + licensePlate;
        }

        // 4. Update Session (Close it)
        boolean sessionClosed = parkingSessionDAO.endSession(session.getSessionId(), timestamp);
        if (!sessionClosed) {
            return "Error: Failed to close parking session.";
        }

        // 5. Log Exit
        EntryExitLog log = new EntryExitLog();
        log.setLicensePlate(licensePlate);
        log.setDirection("EXIT");
        log.setGarageId(1); // Assuming GarageID 1 for simplicity or derive from Spot

        // Retrieve Spot to get GarageID properly
        ParkingSpot spot = parkingSpotDAO.findSpot(session.getSpotId());
        if (spot != null) {
            log.setGarageId(spot.getGarageId());
            log.setSpotId(spot.getSpotID());
        }

        log.setEventTime(timestamp);
        // sensorId not strictly connected here unless passed
        entryExitLogDAO.logEvent(log);

        // 6. Update Spot Status (Free the spot)
        // Find sensor for this spot and update it to 0 (Empty)
        OccupancySensor sensor = occupancySensorDAO.findSensorBySpot(session.getSpotId());
        if (sensor != null) {
            occupancySensorDAO.updateSensorStatus(sensor.getSensorId(), false);
        }

        // 7. Open Gate (Simulated)
        // ExitGate gate = ... open()
        // We just return success message.

        return String.format("Exit Approved for %s. Spot %s freed.",
                licensePlate, (spot != null ? spot.getSpotNumber() : "Unknown"));
    }

    /**
     * Processes a vehicle entry.
     * 
     * @param licensePlate The license plate of the entering vehicle.
     * @param timestamp    The time of entry.
     * @param entryGateId  The ID of the entry gate.
     * @return String confirmation message or error.
     */
    public String processEntry(String licensePlate, Timestamp timestamp, int entryGateId) {
        // 1. Validate Vehicle
        Vehicle vehicle = vehicleDAO.findVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            return "Error: Vehicle not registered: " + licensePlate;
        }

        // 2. Check for Existing Active Session
        ParkingSession activeSession = parkingSessionDAO.findActiveSession(vehicle.getVehicleId());
        if (activeSession != null) {
            return "Error: Vehicle already has an active session (Entry Time: " + activeSession.getEntryTime() + ")";
        }

        // 3. Find Available Spot
        ParkingSpot spot = parkingSpotDAO.findFirstAvailableSpot();
        if (spot == null) {
            return "Error: Garage is FULL.";
        }

        // 4. Create Session
        ParkingSession newSession = new ParkingSession();
        newSession.setVehicleId(vehicle.getVehicleId());
        newSession.setSpotId(spot.getSpotID());
        newSession.setEntryTime(timestamp);

        boolean sessionCreated = parkingSessionDAO.createSession(newSession);
        if (!sessionCreated) {
            return "Error: Failed to create parking session.";
        }

        // 5. Update Spot Occupancy (OccupancySensor)
        // Find the sensor for this spot and set it to occupied
        OccupancySensor sensor = occupancySensorDAO.findSensorBySpot(spot.getSpotID());
        if (sensor != null) {
            occupancySensorDAO.updateSensorStatus(sensor.getSensorId(), true);
        } else {
            // If sensor doesn't exist, we might log a warning, but critical logic relies on
            // session
        }

        // 6. Log Entry
        EntryExitLog log = new EntryExitLog();
        log.setLicensePlate(licensePlate);
        log.setDirection("ENTRY");
        log.setGarageId(spot.getGarageId());
        log.setSpotId(spot.getSpotID());
        log.setEventTime(timestamp);
        entryExitLogDAO.logEvent(log);

        // 7. Open Gate (Simulated)
        EntryGate gate = new EntryGate(); // finding by ID is out of scope for stub
        gate.openGate();

        return String.format("Entry Approved for %s. Assigned Spot: %s",
                licensePlate, spot.getSpotNumber());
    }

    /**
     * Processes a vehicle entry with manual spot selection.
     * 
     * @param licensePlate The license plate.
     * @param timestamp    The entry time.
     * @param entryGateId  The gate ID.
     * @param spotNumber   The requested spot number (e.g. "C-01").
     * @return Result string.
     */
    public String processEntry(String licensePlate, Timestamp timestamp, int entryGateId, String spotNumber) {
        // 1. Validate Vehicle
        Vehicle vehicle = vehicleDAO.findVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            return "Error: Vehicle not registered: " + licensePlate;
        }

        // 2. Check for Existing Active Session
        ParkingSession activeSession = parkingSessionDAO.findActiveSession(vehicle.getVehicleId());
        if (activeSession != null) {
            return "Error: Vehicle already has an active session.";
        }

        // 3. Validate Requested Spot
        ParkingSpot spot = parkingSpotDAO.findSpotByNumber(spotNumber);
        if (spot == null) {
            return "Error: Spot " + spotNumber + " does not exist.";
        }

        // Check if actually available
        OccupancySensor currentSensor = occupancySensorDAO.findSensorBySpot(spot.getSpotID());
        if (currentSensor != null && currentSensor.getOccupancyState()) {
            return "Error: Spot " + spotNumber + " is already OCCUPIED.";
        } else if (currentSensor == null) {
            System.out.println("WARNING: No sensor found for spot " + spotNumber + " (ID: " + spot.getSpotID() + ")");
        }

        // 4. Create Session
        ParkingSession newSession = new ParkingSession();
        newSession.setVehicleId(vehicle.getVehicleId());
        newSession.setSpotId(spot.getSpotID());
        newSession.setEntryTime(timestamp);

        boolean sessionCreated = parkingSessionDAO.createSession(newSession);
        if (!sessionCreated) {
            return "Error: Failed to create parking session.";
        }

        // 5. Update Sensor
        if (currentSensor != null) {
            boolean updated = occupancySensorDAO.updateSensorStatus(currentSensor.getSensorId(), true);
            if (updated) {
                System.out.println("Debug: Sensor " + currentSensor.getSensorId() + " updated to OCCUPIED.");
            } else {
                System.out.println("Error: Failed to update sensor " + currentSensor.getSensorId());
            }
        }

        // 6. Log Entry
        EntryExitLog log = new EntryExitLog();
        log.setLicensePlate(licensePlate);
        log.setDirection("ENTRY");
        log.setGarageId(spot.getGarageId());
        log.setSpotId(spot.getSpotID());
        log.setEventTime(timestamp);
        entryExitLogDAO.logEvent(log);

        // 7. Open Gate
        EntryGate gate = new EntryGate();
        gate.openGate();

        return String.format("Entry Approved for %s. Assigned Spot: %s",
                licensePlate, spot.getSpotNumber());
    }

    /**
     * Registers a new vehicle in the system.
     * 
     * @param licensePlate The license plate.
     * @param type         The vehicle type (e.g., "Visitor").
     * @return Result string.
     */
    public String registerVehicle(String licensePlate, String type) {
        if (vehicleDAO.findVehicleByLicensePlate(licensePlate) != null) {
            return "Error: Vehicle already registered.";
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(licensePlate);
        vehicle.setVehicleType(type);
        vehicle.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        boolean success = vehicleDAO.createVehicle(vehicle);
        if (success) {
            return "Success: Vehicle registered successfully.";
        } else {
            return "Error: Failed to register vehicle.";
        }
    }
}
