package com.augms.controller;

import com.augms.service.VehicleManagementService;
import com.augms.dto.Credentials;

public class VehicleManagementController {
    private VehicleManagementService vehicleManagementService;
    
    public VehicleManagementController(VehicleManagementService vehicleManagementService) {
        this.vehicleManagementService = vehicleManagementService;
    }
    
    public void handleLogin(Credentials credentials) {
        vehicleManagementService.validateCredentials(credentials);
    }
    
    public void handlePendingRegistrationsRequest() {
        vehicleManagementService.getPendingRegistrations();
    }
    
    public void handleUserAction(String userID, String action) {
        if ("accept".equalsIgnoreCase(action)) {
            vehicleManagementService.acceptUser(userID);
        } else if ("reject".equalsIgnoreCase(action)) {
            vehicleManagementService.rejectUser(userID);
        }
    }
}

