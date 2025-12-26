package com.augms.controller;

import com.augms.service.UserRegistrationService;
import com.augms.dto.PersonalDetails;

public class UserRegistrationController {
    private UserRegistrationService userRegistrationService;
    
    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }
    
    public void handleRegistrationRequest(PersonalDetails personalDetails) {
        // Handle user registration request
        userRegistrationService.registerUser(personalDetails);
    }
    
    public void handleRegistrationResult(String status, String message) {
        // Handle registration result
    }
}

