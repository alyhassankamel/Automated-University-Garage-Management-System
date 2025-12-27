package com.augms.presentation;

import com.augms.controller.SensorAlertController;
import com.augms.entity.SensorAlert;
import com.augms.entity.ErrorLog;
import java.util.List;

public class SensorAlertView {
    private SensorAlertController controller;
    
    public SensorAlertView(SensorAlertController controller) {
        this.controller = controller;
    }
    
    public void displaySensorAlert(SensorAlert alert) {
        // Display sensor alert
    }
    
    public void displayErrorLogs(List<ErrorLog> logs) {
        // Display error logs
    }
}

