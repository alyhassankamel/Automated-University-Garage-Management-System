package com.augms.entity;

public class ParkingSpot {
    private String spotID;
    private String status;
    private String garageID;
    // Association: ParkingSpot 1 → OccupancySensor
    private OccupancySensor occupancySensor;
    
    public ParkingSpot() {}
    
    public ParkingSpot(String spotID, String status, String garageID) {
        this.spotID = spotID;
        this.status = status;
        this.garageID = garageID;
    }
    
    public String getSpotID() {
        return spotID;
    }
    
    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getGarageID() {
        return garageID;
    }
    
    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }
    
    public String getOccupancyStatus() {
        return status;
    }
    
    public boolean updateStatus(String status) {
        this.status = status;
        return true;
    }
    
    public OccupancySensor getOccupancySensor() {
        return occupancySensor;
    }
    
    public void setOccupancySensor(OccupancySensor occupancySensor) {
        this.occupancySensor = occupancySensor;
    }
}

