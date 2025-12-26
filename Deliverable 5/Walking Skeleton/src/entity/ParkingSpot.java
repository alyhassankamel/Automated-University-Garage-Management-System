package com.augms.entity;

public class ParkingSpot {
    private String spotID;
    private SpotStatus status;
    private String garageID;
    // Association: ParkingSpot 1 → OccupancySensor
    private OccupancySensor occupancySensor;
    
    public ParkingSpot() {}
    
    public ParkingSpot(String spotID, SpotStatus status, String garageID) {
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
    
    public SpotStatus getStatus() {
        return status;
    }
    
    public void setStatus(SpotStatus status) {
        this.status = status;
    }
    
    public String getGarageID() {
        return garageID;
    }
    
    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }
    
    public SpotStatus getOccupancyStatus() {
        return status;
    }
    
    public boolean updateStatus(SpotStatus status) {
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

