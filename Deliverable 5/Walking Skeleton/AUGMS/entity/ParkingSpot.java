package entity;

/**
 * ParkingSpot represents a single parking space in the garage.
 * It maintains information about its ID, occupancy status, and associated sensor.
 */
public class ParkingSpot {

    /**
     * Default constructor
     */
    public ParkingSpot() {
    }

    /**
     * Constructor with parameters
     */
    public ParkingSpot(int spotId, int garageId, String spotNumber, SpotStatus status) {
        this.spotId = spotId;
        this.garageId = garageId;
        this.spotNumber = spotNumber;
        this.status = status;
    }

    /**
     * Unique spot identifier
     */
    private int spotId;

    /**
     * The garage this spot belongs to
     */
    private int garageId;

    /**
     * Spot number/label (e.g., A-01, B-05)
     */
    private String spotNumber;

    /**
     * Current occupancy status
     */
    private SpotStatus status;

    /**
     * Associated occupancy sensor
     */
    private OccupancySensor occupancySensor;

    /**
     * Gets the unique spot ID
     * @return spot ID
     */
    public int getSpotID() {
        return spotId;
    }

    /**
     * Gets the spot number/label
     * @return spot number string
     */
    public String getSpotNumber() {
        return spotNumber;
    }

    /**
     * Gets the current occupancy status
     * @return the spot status (OCCUPIED, AVAILABLE, OUT_OF_SERVICE)
     */
    public SpotStatus getOccupancyStatus() {
        // If sensor exists, derive status from sensor
        if (occupancySensor != null) {
            return occupancySensor.isOccupied() ? SpotStatus.OCCUPIED : SpotStatus.AVAILABLE;
        }
        return status;
    }

    /**
     * Provides current status to ParkingGarage on request
     * @return the current spot status
     */
    public SpotStatus provideCurrentStatus() {
        return getOccupancyStatus();
    }

    /**
     * Updates spot status
     * @param newStatus the new status to set
     */
    public void updateStatus(SpotStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Gets the associated occupancy sensor
     * @return the OccupancySensor for this spot
     */
    public OccupancySensor getOccupancySensor() {
        return occupancySensor;
    }

    /**
     * Sets the occupancy sensor for this spot
     * @param sensor the sensor to associate
     */
    public void setOccupancySensor(OccupancySensor sensor) {
        this.occupancySensor = sensor;
    }

    // Getters and Setters
    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public void setStatus(SpotStatus status) {
        this.status = status;
    }
}