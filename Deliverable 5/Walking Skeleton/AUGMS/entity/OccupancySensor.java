package entity;

/**
 * OccupancySensor represents a physical sensor that detects vehicle occupancy
 * in a parking spot. It monitors whether a spot is occupied or available.
 */
public class OccupancySensor extends Sensor {

    /**
     * Default constructor
     */
    public OccupancySensor() {
    }

    /**
     * Constructor with parameters
     */
    public OccupancySensor(int sensorId, int spotId, boolean isOccupied) {
        this.sensorId = sensorId;
        this.spotId = spotId;
        this.isOccupied = isOccupied;
    }

    /**
     * Unique sensor identifier
     */
    private int sensorId;

    /**
     * The parking spot this sensor monitors
     */
    private int spotId;

    /**
     * Current occupancy state: true if occupied, false if available
     */
    private boolean isOccupied;

    /**
     * Detects current occupancy state (simulates sensor reading)
     * @return true if spot is occupied, false if available
     */
    public boolean detectOccupancy() {
        // TODO: In real implementation, this would read from physical sensor
        return isOccupied;
    }

    /**
     * Gets the current occupancy state of this sensor
     * @return true if occupied, false if available
     */
    public boolean getOccupancyState() {
        return isOccupied;
    }

    /**
     * Updates the occupancy status of this sensor
     * @param occupied true if spot is now occupied, false if available
     */
    public void updateOccupancyStatus(boolean occupied) {
        this.isOccupied = occupied;
    }

    // Getters and Setters
    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}