package com.augms.controller;

import com.augms.service.RegistrationService;
import com.augms.entity.ParkingUser;
import com.augms.entity.Vehicle;

public class RegistrationController {
    private RegistrationService registrationService;
    
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    
    public void handleRegistrationRequest(ParkingUser parkingUser, Vehicle vehicle) {
        // Handle registration request
        registrationService.validateDetails(parkingUser, vehicle);
        // Process registration
    }
    
    public void handleConfirmation(boolean success, String message) {
        // Handle confirmation
    }
}

