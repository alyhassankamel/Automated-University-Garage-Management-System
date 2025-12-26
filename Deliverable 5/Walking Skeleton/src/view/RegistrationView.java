package com.augms.view;

import com.augms.controller.RegistrationController;
import com.augms.dto.PersonalDetails;
import com.augms.dto.VehicleDetails;

public class RegistrationView {
    private RegistrationController controller;
    
    public RegistrationView(RegistrationController controller) {
        this.controller = controller;
    }
    
    public void submitRegistrationDetails(PersonalDetails personalDetails, VehicleDetails vehicleDetails) {
        controller.handleRegistrationRequest(personalDetails, vehicleDetails);
    }
    
    public void displayConfirmation(boolean success, String message) {
        // Display confirmation to user
    }
    
    public void displayError(String error) {
        // Display error to user
    }
}

