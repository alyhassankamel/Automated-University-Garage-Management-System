package com.augms.dao;

import com.augms.entity.ParkingSpot;
import com.augms.entity.SpotStatus;
import java.util.List;

public interface ParkingSpotDAO {
    ParkingSpot findSpot(String spotID);
    List<ParkingSpot> getAllSpots();
    List<ParkingSpot> getAvailableSpots();
    List<ParkingSpot> getOccupiedSpots();
    boolean updateSpotStatus(String spotID, SpotStatus status);
    List<ParkingSpot> getSpotsByGarage(String garageID);
}

