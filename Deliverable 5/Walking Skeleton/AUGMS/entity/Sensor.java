package entity;

/**
 * Base sensor class
 */
public class Sensor {

    /**
     * Default constructor
     */
    public Sensor() {
    }

    /**
     * Sensor ID
     */
    private String sensorID;

    /**
     * Gets the sensor ID
     * @return sensor ID
     */
    public String getSensorID() {
        return sensorID;
    }

    /**
     * Sets the sensor ID
     * @param sensorID the sensor ID to set
     */
    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }
}