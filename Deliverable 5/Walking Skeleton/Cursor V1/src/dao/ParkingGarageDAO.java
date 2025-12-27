package com.augms.dao;

import com.augms.entity.ParkingGarage;
import java.util.List;

public interface ParkingGarageDAO {
    ParkingGarage findGarage(String garageID);
    List<ParkingGarage> getAllGarages();
    boolean updateGarage(ParkingGarage garage);
    int getAvailableSpotCount(String garageID);
    int getOccupiedSpotCount(String garageID);
    boolean incrementOccupiedSpots(String garageID);
    boolean decrementOccupiedSpots(String garageID);
}

