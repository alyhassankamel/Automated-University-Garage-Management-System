package com.augms.view;

import com.augms.controller.UserRegistrationController;
import com.augms.dto.PersonalDetails;

public class UserRegistrationView {
    private UserRegistrationController controller;
    
    public UserRegistrationView(UserRegistrationController controller) {
        this.controller = controller;
    }
    
    public void submitRegistrationRequest(PersonalDetails personalDetails) {
        controller.handleRegistrationRequest(personalDetails);
    }
    
    public void displayRegistrationResult(String status, String message) {
        // Display registration result (success / pending / error)
    }
    
    public void displayError(String error) {
        // Display error message
    }
}

