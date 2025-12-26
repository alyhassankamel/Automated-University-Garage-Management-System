package com.augms.entity;

import java.util.Date;

public class OccupancyData {
    private String spotID;
    private boolean isOccupied;
    private Date timestamp;
    
    public OccupancyData() {
        this.timestamp = new Date();
    }
    
    public OccupancyData(String spotID, boolean isOccupied, Date timestamp) {
        this.spotID = spotID;
        this.isOccupied = isOccupied;
        this.timestamp = timestamp;
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
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

