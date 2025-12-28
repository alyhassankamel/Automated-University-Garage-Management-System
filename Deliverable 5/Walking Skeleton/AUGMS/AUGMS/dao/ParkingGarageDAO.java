package AUGMS.dao;

import AUGMS.entity.ParkingGarage;
import java.io.*;
import java.util.*;

/**
 * Data Access Object for ParkingGarage
 */
public class ParkingGarageDAO {

    private Map<String, ParkingGarage> garages;

    /**
     * Default constructor
     */
    public ParkingGarageDAO() {
        this.garages = new HashMap<>();
    }

    /**
     * @param garageID
     * @return
     */
    public ParkingGarage findGarage(String garageID) {
        return garages.get(garageID);
    }

    /**
     * @return
     */
    public Set<ParkingGarage> getAllGarages() {
        return new HashSet<>(garages.values());
    }

    /**
     * @param garage
     * @return
     */
    public void updateGarage(ParkingGarage garage) {
        if (garage != null) {
            garages.put(garage.getGarageID(), garage);
        }
    }

    public void saveGarage(ParkingGarage garage) {
        updateGarage(garage);
    }

    /**
     * @param garageID
     * @return
     */
    public int getAvailableSpotCount(String garageID) {
        ParkingGarage garage = findGarage(garageID);
        return garage != null ? garage.getAvailableSpotCount() : 0;
    }

    /**
     * @param garageID
     * @return
     */
    public int getOccupiedSpotCount(String garageID) {
        ParkingGarage garage = findGarage(garageID);
        return garage != null ? garage.countOccupiedSlots() : 0;
    }

    /**
     * @param garageID
     * @return
     */
    public void incrementOccupiedSpots(String garageID) {
        ParkingGarage garage = findGarage(garageID);
        if (garage != null && garage.getAvailableSpotCount() > 0) {
            garage.setAvailableSpotCount(garage.getAvailableSpotCount() - 1);
            updateGarage(garage);
        }
    }

    /**
     * ...
     * @param garageID
     * @return
     */
    public void decrementOccupiedSpots(String garageID) {
        ParkingGarage garage = findGarage(garageID);
        if (garage != null && garage.getAvailableSpotCount() < garage.getTotalSpots()) {
            garage.setAvailableSpotCount(garage.getAvailableSpotCount() + 1);
            updateGarage(garage);
        }
    }

}
