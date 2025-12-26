package com.augms.controller;

import com.augms.service.RegistrationService;
import com.augms.dto.PersonalDetails;
import com.augms.dto.VehicleDetails;

public class RegistrationController {
    private RegistrationService registrationService;
    
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    
    public void handleRegistrationRequest(PersonalDetails personalDetails, VehicleDetails vehicleDetails) {
        // Handle registration request
        registrationService.validateDetails(personalDetails, vehicleDetails);
        // Process registration
    }
    
    public void handleConfirmation(boolean success, String message) {
        // Handle confirmation
    }
}

