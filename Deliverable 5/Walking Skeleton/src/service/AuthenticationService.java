package com.augms.service;

import com.augms.dao.ParkingUserDAO;
import com.augms.dao.GarageAdminDAO;
import com.augms.dto.Credentials;

public class AuthenticationService {
    private ParkingUserDAO parkingUserDAO;
    private GarageAdminDAO garageAdminDAO;
    
    public AuthenticationService(ParkingUserDAO parkingUserDAO, GarageAdminDAO garageAdminDAO) {
        this.parkingUserDAO = parkingUserDAO;
        this.garageAdminDAO = garageAdminDAO;
    }
    
    public boolean validateCredentials(Credentials credentials) {
        // Validate user credentials
        return garageAdminDAO.validateCredentials(credentials.getUserID(), credentials.getPassword());
    }
    
    public boolean authenticateUser(String userID, String password) {
        return garageAdminDAO.validateCredentials(userID, password);
    }
    
    public boolean checkAccessPermissions(String userID, String resource) {
        // Check user access permissions
        return true;
    }
}

