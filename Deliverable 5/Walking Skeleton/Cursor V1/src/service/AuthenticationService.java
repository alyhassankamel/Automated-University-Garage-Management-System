package com.augms.service;

import com.augms.dao.ParkingUserDAO;
import com.augms.dao.GarageAdminDAO;

public class AuthenticationService {
    private ParkingUserDAO parkingUserDAO;
    private GarageAdminDAO garageAdminDAO;
    
    public AuthenticationService(ParkingUserDAO parkingUserDAO, GarageAdminDAO garageAdminDAO) {
        this.parkingUserDAO = parkingUserDAO;
        this.garageAdminDAO = garageAdminDAO;
    }
    
    public boolean validateCredentials(String userID, String password) {
        // Validate user credentials
        return garageAdminDAO.validateCredentials(userID, password);
    }
    
    public boolean authenticateUser(String userID, String password) {
        return garageAdminDAO.validateCredentials(userID, password);
    }
    
    public boolean checkAccessPermissions(String userID, String resource) {
        // Check user access permissions
        return true;
    }
}

