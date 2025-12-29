package entity;

import java.sql.Timestamp;

/**
 * Entity class for Vehicle table.
 */
public class Vehicle {

    private int vehicleId;
    private String licensePlate;
    private String vehicleType;
    private Timestamp registeredAt;

    public Vehicle() {
    }

    public Vehicle(int vehicleId, String licensePlate, String vehicleType, Timestamp registeredAt) {
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.registeredAt = registeredAt;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", licensePlate='" + licensePlate + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}