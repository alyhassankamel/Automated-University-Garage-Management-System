package com.augms.entity;

import java.util.Date;

public class ParkingStatus {
    private String garageID;
    private int availableSpots;
    private int occupiedSpots;
    private int totalSpots;
    private Date timestamp;
    
    public ParkingStatus() {
        this.timestamp = new Date();
    }
    
    public ParkingStatus(String garageID, int availableSpots, int occupiedSpots, int totalSpots) {
        this.garageID = garageID;
        this.availableSpots = availableSpots;
        this.occupiedSpots = occupiedSpots;
        this.totalSpots = totalSpots;
        this.timestamp = new Date();
    }
    
    public String getGarageID() {
        return garageID;
    }
    
    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }
    
    public int getAvailableSpots() {
        return availableSpots;
    }
    
    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }
    
    public int getOccupiedSpots() {
        return occupiedSpots;
    }
    
    public void setOccupiedSpots(int occupiedSpots) {
        this.occupiedSpots = occupiedSpots;
    }
    
    public int getTotalSpots() {
        return totalSpots;
    }
    
    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

