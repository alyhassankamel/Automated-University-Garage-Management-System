package com.augms.service;

import com.augms.dao.ParkingUserDAO;
import com.augms.dto.PersonalDetails;
import com.augms.entity.ParkingUser;
import com.augms.entity.RegistrationResult;

public class UserRegistrationService {
    private ParkingUserDAO parkingUserDAO;
    private LoggingService loggingService;
    
    public UserRegistrationService(ParkingUserDAO parkingUserDAO, LoggingService loggingService) {
        this.parkingUserDAO = parkingUserDAO;
        this.loggingService = loggingService;
    }
    
    public RegistrationResult registerUser(PersonalDetails personalDetails) {
        // Validate input data
        if (!validateInputData(personalDetails)) {
            return new RegistrationResult(false, "Invalid input data");
        }
        
        // Create user account
        ParkingUser user = createUserAccount(personalDetails);
        
        // Set registration status
        user.setRegistrationStatus(com.augms.entity.RegistrationStatus.PENDING);
        
        // Save user
        boolean saved = parkingUserDAO.createUser(user);
        
        if (saved) {
            // Record registration event
            loggingService.recordRegistration(null);
            return new RegistrationResult(true, "Registration successful - pending approval");
        } else {
            return new RegistrationResult(false, "Registration failed");
        }
    }
    
    private boolean validateInputData(PersonalDetails personalDetails) {
        // Validate input data
        return personalDetails != null 
            && personalDetails.getUniID() != null 
            && !personalDetails.getUniID().isEmpty()
            && personalDetails.getEmail() != null 
            && !personalDetails.getEmail().isEmpty();
    }
    
    private ParkingUser createUserAccount(PersonalDetails personalDetails) {
        // Create user account from personal details
        ParkingUser user = new ParkingUser();
        user.setUniID(personalDetails.getUniID());
        user.setName(personalDetails.getName());
        user.setEmail(personalDetails.getEmail());
        user.setPhone(personalDetails.getPhone());
        user.setRegistrationStatus(com.augms.entity.RegistrationStatus.PENDING);
        user.setAccessStatus(false);
        return user;
    }
    
    public void notifyUser(String userID, String status, String message) {
        // Notify user of registration result
    }
}

