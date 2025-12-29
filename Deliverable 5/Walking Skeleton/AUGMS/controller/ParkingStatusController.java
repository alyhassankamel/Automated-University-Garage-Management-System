package controller;

import entity.*;
import java.util.*;
import service.ParkingStatusService;

/**
 * Controller for handling parking status requests.
 * Mediates between user requests and service layer.
 * Orchestrates calls to ParkingStatusService to provide real-time parking information.
 */
public class ParkingStatusController {

    /**
     * Service for parking status operations
     */
    private ParkingStatusService parkingStatusService;

    /**
     * Constructor with default service
     */
    public ParkingStatusController() {
        this.parkingStatusService = new ParkingStatusService();
    }

    /**
     * Constructor with custom service (for testing)
     */
    public ParkingStatusController(ParkingStatusService service) {
        this.parkingStatusService = service;
    }

    /**
     * Handles a request to view current parking occupancy in real-time
     * @param garageId the garage ID to query
     * @return ParkingStatus with current occupancy information
     */
    public ParkingStatus handleLiveOccupancyRequest(int garageId) {
        // Check if user has access to view this garage
        if (!parkingStatusService.isGarageAccessible(garageId)) {
            System.err.println("Garage " + garageId + " is not accessible");
            return null;
        }

        // Get current status
        ParkingStatus status = parkingStatusService.getParkingStatus(garageId);
        
        if (status != null) {
            System.out.println("Retrieved parking status for garage: " + status.getGarageName());
            System.out.println("  Available: " + status.getAvailableSpots() + 
                             " / " + status.getTotalSpots());
            System.out.println("  Occupied: " + status.getOccupiedSpots());
            System.out.println("  Occupancy: " + String.format("%.1f%%", status.getOccupancyPercentage()));
        }
        
        return status;
    }

    /**
     * Handles a status update when sensor detects occupancy change
     * @param sensorId the sensor ID
     * @param isOccupied the new occupancy state
     * @return true if update was successful
     */
    public boolean handleStatusUpdate(int sensorId, boolean isOccupied) {
        return parkingStatusService.updateSensorReading(sensorId, isOccupied);
    }

    /**
     * Handles a request to get available parking spots
     * @param garageId the garage ID
     * @return set of available parking spots
     */
    public Set<ParkingSpot> getAvailableSpots(int garageId) {
        return parkingStatusService.getAvailableSpots(garageId);
    }

    /**
     * Handles a request to get occupied parking spots
     * @param garageId the garage ID
     * @return set of occupied parking spots
     */
    public Set<ParkingSpot> getOccupiedSpots(int garageId) {
        return parkingStatusService.getOccupiedSpots(garageId);
    }

    /**
     * Handles a request to count available slots
     * @param garageId the garage ID
     * @return number of available slots
     */
    public int getAvailableSlotCount(int garageId) {
        return parkingStatusService.countAvailableSlots(garageId);
    }

    /**
     * Handles a request to count occupied slots
     * @param garageId the garage ID
     * @return number of occupied slots
     */
    public int getOccupiedSlotCount(int garageId) {
        return parkingStatusService.countOccupiedSlots(garageId);
    }

    /**
     * Handles a request to get all garages with their status
     * @return set of all parking garages with current status
     */
    public Set<ParkingGarage> getAllGaragesStatus() {
        return parkingStatusService.getAllGaragesWithStatus();
    }

    /**
     * Handles a request to check garage accessibility
     * @param garageId the garage ID
     * @return true if accessible, false otherwise
     */
    public boolean isGarageAccessible(int garageId) {
        return parkingStatusService.isGarageAccessible(garageId);
    }

    /**
     * Handles access permission check before returning detailed status
     * @param garageId the garage ID
     * @param userId the user ID requesting access
     * @return true if user has access, false otherwise
     */
    public boolean verifyUserAccess(int garageId, String userId) {
        return parkingStatusService.checkAccessPermissions(garageId, userId);
    }

    /**
     * Handles a request to get real-time occupancy of a specific spot
     * @param spotId the spot ID
     * @return true if occupied, false if available
     */
    public boolean getSpotOccupancy(int spotId) {
        return parkingStatusService.getSpotOccupancy(spotId);
    }
}