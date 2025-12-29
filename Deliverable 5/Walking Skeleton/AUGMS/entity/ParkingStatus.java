package entity;

import java.time.LocalDateTime;

/**
 * ParkingStatus represents a snapshot of parking availability in a garage.
 * It aggregates counts and status information for display to users.
 */
public class ParkingStatus {

    /**
     * Default constructor
     */
    public ParkingStatus() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with parameters
     */
    public ParkingStatus(int garageId, String garageName, int totalSpots, 
                         int availableSpots, int occupiedSpots, boolean isAccessible) {
        this.garageId = garageId;
        this.garageName = garageName;
        this.totalSpots = totalSpots;
        this.availableSpots = availableSpots;
        this.occupiedSpots = occupiedSpots;
        this.isAccessible = isAccessible;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Garage identifier
     */
    private int garageId;

    /**
     * Garage name
     */
    private String garageName;

    /**
     * Number of available spots
     */
    private int availableSpots;

    /**
     * Number of occupied spots
     */
    private int occupiedSpots;

    /**
     * Total number of spots in garage
     */
    private int totalSpots;

    /**
     * Whether garage is accessible
     */
    private boolean isAccessible;

    /**
     * Timestamp when status was captured
     */
    private LocalDateTime timestamp;

    /**
     * Gets the available spot count
     * @return number of available spots
     */
    public int getAvailableSpots() {
        return availableSpots;
    }

    /**
     * Sets the available spot count
     * @param count number of available spots
     */
    public void setAvailableSpots(int count) {
        this.availableSpots = count;
    }

    /**
     * Gets the occupied spot count
     * @return number of occupied spots
     */
    public int getOccupiedSpots() {
        return occupiedSpots;
    }

    /**
     * Sets the occupied spot count
     * @param count number of occupied spots
     */
    public void setOccupiedSpots(int count) {
        this.occupiedSpots = count;
    }

    /**
     * Gets the total spot count
     * @return total number of spots
     */
    public int getTotalSpots() {
        return totalSpots;
    }

    /**
     * Sets the total spot count
     * @param count total number of spots
     */
    public void setTotalSpots(int count) {
        this.totalSpots = count;
    }

    // Getters and Setters
    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setIsAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Calculates occupancy percentage
     * @return occupancy rate as percentage (0-100)
     */
    public double getOccupancyPercentage() {
        if (totalSpots == 0) return 0;
        return (double) occupiedSpots / totalSpots * 100;
    }

    /**
     * Calculates availability percentage
     * @return availability rate as percentage (0-100)
     */
    public double getAvailabilityPercentage() {
        if (totalSpots == 0) return 0;
        return (double) availableSpots / totalSpots * 100;
    }

    @Override
    public String toString() {
        return "ParkingStatus{" +
                "garageId=" + garageId +
                ", garageName='" + garageName + '\'' +
                ", availableSpots=" + availableSpots +
                ", occupiedSpots=" + occupiedSpots +
                ", totalSpots=" + totalSpots +
                ", isAccessible=" + isAccessible +
                ", timestamp=" + timestamp +
                '}';
    }
}