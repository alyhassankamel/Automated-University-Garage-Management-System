package AUGMS.entity;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ParkingGarage {

    /**
     * Default constructor
     */
    public ParkingGarage() {
    }

    /**
     * 
     */
    private String garageID;

    /**
     * 
     */
    private GarageAccess garageAccess;

    /**
     * 
     */
    private int availableSpotCount;

    /**
     * 
     */
    private int totalSpots;

    private List<ParkingSpot> parkingSpots;



    // Constructors
    public ParkingGarage(String garageID, int totalSpots) {
        this.garageID = garageID;
        this.totalSpots = totalSpots;
        this.availableSpotCount = totalSpots;
        this.garageAccess = GarageAccess.NORMAL;
        this.parkingSpots = new ArrayList<>();
    }

    /**
     * @return
     */
    public String getGarageID() {
        return garageID;
    }

    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }

    public GarageAccess getGarageAccess() {
        return garageAccess;
    }

    public void setGarageAccess(GarageAccess garageAccess) {
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

    /**
     * @return
     */
    public int countAvailableSlots() {
        return availableSpotCount;
    }

    /**
     * @return
     */
    public int countOccupiedSlots() {
        return totalSpots - availableSpotCount;
    }

    /**
     * @return
     */
    public Set<ParkingSpot> getAvailableSpots() {
        Set<ParkingSpot> available = new HashSet<>();
        if (parkingSpots != null) {
            for (ParkingSpot spot : parkingSpots) {
                if (spot.getStatus() == SpotStatus.AVAILABLE) {
                    available.add(spot);
                }
            }
        }
        return available;
    }

    /**
     * @return
     */
    public Set<ParkingSpot> getOccupiedSpots() {
        Set<ParkingSpot> occupied = new HashSet<>();
        if (parkingSpots != null) {
            for (ParkingSpot spot : parkingSpots) {
                if (spot.getStatus() == SpotStatus.OCCUPIED) {
                    occupied.add(spot);
                }
            }
        }
        return occupied;
    }

    /**
     * @return
     */
    public ParkingStatus getParkingStatus() {
        return new ParkingStatus(garageID, availableSpotCount, countOccupiedSlots(), totalSpots);
    }

    /**
     * @return
     */
    public boolean isGarageAccessible() {
        return garageAccess != GarageAccess.CLOSED;
    }

}
