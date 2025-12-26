package com.augms.dao;

import com.augms.entity.GateSensor;
import com.augms.entity.GateStatus;

public interface GateSensorDAO {
    GateSensor findSensor(String sensorID);
    String scanLicensePlate(String sensorID);
    boolean detectGateError(String sensorID);
    GateStatus getGateStatus(String sensorID);
}

