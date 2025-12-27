package com.augms.entity;

import java.util.List;

public class ParkingGarage {
    private String garageID;
    private List<ParkingSpot> parkingSlots;
    private boolean garageAccess;
    private int availableSpotCount;
    private int totalSpots;
    
    public ParkingGarage() {}
    
    public ParkingGarage(String garageID, List<ParkingSpot> parkingSlots, 
                        boolean garageAccess, int availableSpotCount, int totalSpots) {
        this.garageID = garageID;
        this.parkingSlots = parkingSlots;
        this.garageAccess = garageAccess;
        this.availableSpotCount = availableSpotCount;
        this.totalSpots = totalSpots;
    }
    
    public String getGarageID() {
        return garageID;
    }
    
    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }
    
    public List<ParkingSpot> getParkingSlots() {
        return parkingSlots;
    }
    
    public void setParkingSlots(List<ParkingSpot> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }
    
    public boolean isGarageAccess() {
        return garageAccess;
    }
    
    public void setGarageAccess(boolean garageAccess) {
        this.garageAccess = garageAccess;
    }
    
    public int getAvailableSpotCount() {
        return availableSpotCount;
    }
    
    public void setAvailableSpotCount(int availableSpotCount) {
        this.availableSpotCount = availableSpotCount;
    }
    
    public int getTotalSpots() {
        return totalSpots;
    }
    
    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }
    
    // Association: ParkingGarage 1..* → ParkingSpot (already defined as parkingSlots)
    
    public int countAvailableSlots() {
        return availableSpotCount;
    }
    
    public int countOccupiedSpots() {
        return totalSpots - availableSpotCount;
    }
    
    public List<ParkingSpot> getAvailableSpots() {
        return parkingSlots.stream()
            .filter(spot -> "FREE".equals(spot.getStatus()))
            .toList();
    }
    
    public List<ParkingSpot> getOccupiedSpots() {
        return parkingSlots.stream()
            .filter(spot -> "OCCUPIED".equals(spot.getStatus()))
            .toList();
    }
    
    public ParkingStatus getParkingStatus() {
        return new ParkingStatus(garageID, availableSpotCount, 
                               countOccupiedSpots(), totalSpots);
    }
    
    public boolean isGarageAccessible() {
        return garageAccess;
    }
}

