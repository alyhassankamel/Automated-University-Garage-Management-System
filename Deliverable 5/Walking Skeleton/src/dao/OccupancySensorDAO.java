package com.augms.dao;

import com.augms.entity.OccupancySensor;
import java.util.List;

public interface OccupancySensorDAO {
    OccupancySensor findSensor(String sensorID);
    OccupancySensor findSensorBySpot(String spotID);
    List<OccupancySensor> getAllSensors();
    boolean updateSensorStatus(String sensorID, boolean isOccupied);
    boolean detectOccupancy(String sensorID);
    boolean getOccupancyState(String sensorID);
}

