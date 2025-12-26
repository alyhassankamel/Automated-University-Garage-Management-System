package com.augms.entity;

public class SensorData {
    private String sensorID;
    private boolean isOccupied;
    private String spotID;
    
    public SensorData(String sensorID, boolean isOccupied, String spotID) {
        this.sensorID = sensorID;
        this.isOccupied = isOccupied;
        this.spotID = spotID;
    }
    
    public String getSensorID() {
        return sensorID;
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }
    
    public String getSpotID() {
        return spotID;
    }
}

