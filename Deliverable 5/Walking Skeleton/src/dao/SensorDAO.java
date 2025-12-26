package com.augms.dao;

import com.augms.entity.Sensor;
import com.augms.entity.SensorStatus;
import java.util.List;

public interface SensorDAO {
    Sensor findSensor(String sensorID);
    List<Sensor> getAllSensors();
    boolean updateSensorStatus(String sensorID, SensorStatus status);
}

