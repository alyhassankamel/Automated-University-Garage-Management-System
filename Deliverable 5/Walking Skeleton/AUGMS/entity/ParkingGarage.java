package entity;

import java.io.*;
import java.util.*;

/**
 * ParkingGarage represents a parking facility in the university.
 * It aggregates parking spots and sensors, providing real-time occupancy information.
 * It also manages access control and generates status reports for users.
 */
public class ParkingGarage {

    /**
     * Default constructor
     */
    public ParkingGarage() {
        this.parkingSpots = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     */
    public ParkingGarage(int garageId, String garageName, int totalSpots, boolean isAccessible) {
        this.garageId = garageId;
        this.garageName = garageName;
        this.totalSpots = totalSpots;
        this.isAccessible = isAccessible;
        this.parkingSpots = new ArrayList<>();
    }

    /**
     * Unique garage identifier
     */
    private int garageId;

    /**
     * Garage name/description
     */
    private String garageName;

    /**
     * Whether the garage is currently accessible
     */
    private boolean isAccessible;

    /**
     * Total number of spots in this garage
     */
    private int totalSpots;

    /**
     * Count of available spots (cached)
     */
    private int availableSpotCount;

    /**
     * List of all parking spots in this garage
     */
    private List<ParkingSpot> parkingSpots;

    /**
     * Gets the unique garage ID
     * @return garage ID
     */
    public int getGarageID() {
        return garageId;
    }

    /**
     * Gets the garage name
     * @return garage name
     */
    public String getGarageName() {
        return garageName;
    }

    /**
     * Counts available (unoccupied) slots
     * @return number of available slots
     */
    public int countAvailableSlots() {
        int count = 0;
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getOccupancyStatus() == SpotStatus.AVAILABLE) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts occupied slots
     * @return number of occupied slots
     */
    public int countOccupiedSlots() {
        int count = 0;
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getOccupancyStatus() == SpotStatus.OCCUPIED) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets all available spots
     * @return set of available parking spots
     */
    public Set<ParkingSpot> getAvailableSpots() {
        Set<ParkingSpot> availableSpots = new HashSet<>();
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getOccupancyStatus() == SpotStatus.AVAILABLE) {
                availableSpots.add(spot);
            }
        }
        return availableSpots;
    }

    /**
     * Gets all occupied spots
     * @return set of occupied parking spots
     */
    public Set<ParkingSpot> getOccupiedSpots() {
        Set<ParkingSpot> occupiedSpots = new HashSet<>();
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getOccupancyStatus() == SpotStatus.OCCUPIED) {
                occupiedSpots.add(spot);
            }
        }
        return occupiedSpots;
    }

    /**
     * Checks if garage is currently accessible
     * @return true if accessible, false otherwise
     */
    public boolean isGarageAccessible() {
        return isAccessible;
    }

    /**
     * Sets garage accessibility
     * @param accessible whether garage should be accessible
     */
    public void setGarageAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    /**
     * Gets aggregated parking status for display to user
     * Includes: available count, occupied count, total spots, and accessibility
     * @return ParkingStatus object with current garage status
     */
    public ParkingStatus getParkingStatus() {
        ParkingStatus status = new ParkingStatus();
        status.setGarageId(garageId);
        status.setGarageName(garageName);
        status.setTotalSpots(totalSpots);
        status.setAvailableSpots(countAvailableSlots());
        status.setOccupiedSpots(countOccupiedSlots());
        status.setIsAccessible(isAccessible);
        return status;
    }

    /**
     * Gets all parking spots in this garage
     * @return list of all parking spots
     */
    public List<ParkingSpot> getParkingSpots() {
        return new ArrayList<>(parkingSpots);
    }

    /**
     * Adds a parking spot to this garage
     * @param spot the spot to add
     */
    public void addParkingSpot(ParkingSpot spot) {
        parkingSpots.add(spot);
    }

    /**
     * Gets a specific parking spot by ID
     * @param spotId the spot ID
     * @return the parking spot or null if not found
     */
    public ParkingSpot getParkingSpot(int spotId) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getSpotID() == spotId) {
                return spot;
            }
        }
        return null;
    }

    // Getters and Setters
    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public int getAvailableSpotCount() {
        return countAvailableSlots();
    }

    public void setParkingSpots(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }
}