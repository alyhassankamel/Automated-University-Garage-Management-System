package service;

import dao.*;
import entity.*;

import java.io.*;
import java.util.*;

/**
 * Service layer for parking status operations.
 * Aggregates data from multiple DAOs and prepares it for display to users.
 */
public class ParkingStatusService {

    /**
     * DAO for garage operations
     */
    private ParkingGarageDAO parkingGarageDAO;

    /**
     * DAO for parking spot operations
     */
    private ParkingSpotDAO parkingSpotDAO;

    /**
     * DAO for occupancy sensor operations
     */
    private OccupancySensorDAO occupancySensorDAO;

    /**
     * Constructor with dependency injection
     */
    public ParkingStatusService() {
        this.parkingGarageDAO = new ParkingGarageDAO();
        this.parkingSpotDAO = new ParkingSpotDAO();
        this.occupancySensorDAO = new OccupancySensorDAO();
    }

    /**
     * Constructor with custom DAOs (for testing)
     */
    public ParkingStatusService(ParkingGarageDAO garageDAO, ParkingSpotDAO spotDAO, OccupancySensorDAO sensorDAO) {
        this.parkingGarageDAO = garageDAO;
        this.parkingSpotDAO = spotDAO;
        this.occupancySensorDAO = sensorDAO;
    }

    /**
     * Gets the current parking status for a specific garage
     * @param garageId the garage ID
     * @return ParkingStatus object with current data
     */
    public ParkingStatus checkCurrentStatus(int garageId) {
        return parkingGarageDAO.getParkingStatus(garageId);
    }

    /**
     * Aggregates parking data for a garage including available and occupied counts
     * @param garageId the garage ID
     * @return ParkingStatus with aggregated data
     */
    public ParkingStatus aggregateData(int garageId) {
        ParkingGarage garage = parkingGarageDAO.findGarage(garageId);
        if (garage == null) {
            return null;
        }
        return garage.getParkingStatus();
    }

    /**
     * Gets all available parking spots in a garage
     * @param garageId the garage ID
     * @return set of available ParkingSpot objects
     */
    public Set<ParkingSpot> getAvailableSpots(int garageId) {
        Set<ParkingSpot> allSpots = parkingSpotDAO.getSpotsByGarage(garageId);
        Set<ParkingSpot> availableSpots = new HashSet<>();
        
        for (ParkingSpot spot : allSpots) {
            if (spot.getOccupancyStatus() == SpotStatus.AVAILABLE) {
                availableSpots.add(spot);
            }
        }
        return availableSpots;
    }

    /**
     * Gets all occupied parking spots in a garage
     * @param garageId the garage ID
     * @return set of occupied ParkingSpot objects
     */
    public Set<ParkingSpot> getOccupiedSpots(int garageId) {
        Set<ParkingSpot> allSpots = parkingSpotDAO.getSpotsByGarage(garageId);
        Set<ParkingSpot> occupiedSpots = new HashSet<>();
        
        for (ParkingSpot spot : allSpots) {
            if (spot.getOccupancyStatus() == SpotStatus.OCCUPIED) {
                occupiedSpots.add(spot);
            }
        }
        return occupiedSpots;
    }

    /**
     * Counts available parking slots in a garage
     * @param garageId the garage ID
     * @return number of available slots
     */
    public int countAvailableSlots(int garageId) {
        return parkingGarageDAO.getAvailableSpotCount(garageId);
    }

    /**
     * Counts occupied parking slots in a garage
     * @param garageId the garage ID
     * @return number of occupied slots
     */
    public int countOccupiedSlots(int garageId) {
        return parkingGarageDAO.getOccupiedSpotCount(garageId);
    }

    /**
     * Gets comprehensive parking status for a garage
     * @param garageId the garage ID
     * @return ParkingStatus object with all relevant data
     */
    public ParkingStatus getParkingStatus(int garageId) {
        return checkCurrentStatus(garageId);
    }

    /**
     * Checks if a garage is currently accessible
     * @param garageId the garage ID
     * @return true if accessible, false otherwise
     */
    public boolean isGarageAccessible(int garageId) {
        ParkingGarage garage = parkingGarageDAO.findGarage(garageId);
        return garage != null && garage.isGarageAccessible();
    }

    /**
     * Checks access permissions before returning detailed status
     * @param garageId the garage ID
     * @param userId the user ID (could extend this to check user permissions)
     * @return true if user has access, false otherwise
     */
    public boolean checkAccessPermissions(int garageId, String userId) {
        // TODO: Implement proper access control logic
        // For now, all users can view any garage
        return isGarageAccessible(garageId);
    }

    /**
     * Gets all garages with their current status
     * @return set of ParkingGarages with aggregated status
     */
    public Set<ParkingGarage> getAllGaragesWithStatus() {
        return parkingGarageDAO.getAllGarages();
    }

    /**
     * Gets real-time occupancy information for a specific spot
     * @param spotId the spot ID
     * @return true if occupied, false if available
     */
    public boolean getSpotOccupancy(int spotId) {
        ParkingSpot spot = parkingSpotDAO.findSpot(spotId);
        return spot != null && spot.getOccupancyStatus() == SpotStatus.OCCUPIED;
    }

    /**
     * Updates sensor reading for a spot
     * @param sensorId the sensor ID
     * @param isOccupied the new occupancy state
     * @return true if update successful, false otherwise
     */
    public boolean updateSensorReading(int sensorId, boolean isOccupied) {
        return occupancySensorDAO.updateSensorStatus(sensorId, isOccupied);
    }
}