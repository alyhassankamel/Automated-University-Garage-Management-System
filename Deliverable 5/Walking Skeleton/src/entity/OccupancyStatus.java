package com.augms.entity;

public class OccupancyStatus {
    private int availableSpots;
    private int occupiedSpots;
    private int totalSpots;
    
    public OccupancyStatus(int availableSpots, int occupiedSpots, int totalSpots) {
        this.availableSpots = availableSpots;
        this.occupiedSpots = occupiedSpots;
        this.totalSpots = totalSpots;
    }
    
    public int getAvailableSpots() {
        return availableSpots;
    }
    
    public int getOccupiedSpots() {
        return occupiedSpots;
    }
    
    public int getTotalSpots() {
        return totalSpots;
    }
}

