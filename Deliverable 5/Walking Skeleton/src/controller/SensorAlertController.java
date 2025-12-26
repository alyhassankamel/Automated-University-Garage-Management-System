package com.augms.controller;

import com.augms.service.SensorAlertService;
import com.augms.entity.SensorAlert;

public class SensorAlertController {
    private SensorAlertService sensorAlertService;
    
    public SensorAlertController(SensorAlertService sensorAlertService) {
        this.sensorAlertService = sensorAlertService;
    }
    
    public void handleSensorAlert(SensorAlert alert) {
        sensorAlertService.receiveSensorErrorAlert(alert);
    }
    
    public void handleErrorLogRequest() {
        // Handle error log request
    }
}

