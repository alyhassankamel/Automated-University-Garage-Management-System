package com.augms.entity;

public class OccupancySensor extends Sensor {
    private String spotID;
    private boolean isOccupied;
    
    public OccupancySensor() {
        super();
    }
    
    public OccupancySensor(String sensorID, String spotID, boolean isOccupied) {
        super(sensorID);
        this.spotID = spotID;
        this.isOccupied = isOccupied;
    }
    
    public String getSpotID() {
        return spotID;
    }
    
    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }
    
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    
    public boolean detectOccupancy() {
        // Sensor logic to detect occupancy
        return isOccupied;
    }
    
    public boolean getOccupancyState() {
        return isOccupied;
    }
    
    public boolean updateOccupancyStatus(boolean status) {
        this.isOccupied = status;
        return true;
    }
    
    public boolean notifyParkingSpotOnStatusChange() {
        // Notify parking spot of status change
        return true;
    }
    
    public boolean detectOccupancyError() {
        // Detect occupancy sensor errors
        return false;
    }
    
    public SensorAlert generateOccupancyErrorAlert() {
        return new SensorAlert(null, getSensorID(), "OCCUPANCY_ERROR", null, "HIGH", "Occupancy sensor error detected");
    }
    
    public boolean transmitAlert(SensorAlert alert) {
        // Transmit alert to logging system
        return true;
    }
}

