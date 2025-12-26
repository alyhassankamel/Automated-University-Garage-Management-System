package com.augms.service;

import com.augms.dao.ParkingUserDAO;
import com.augms.entity.ParkingUser;
import com.augms.entity.AccessChangeResult;

public class UserAccessService {
    private ParkingUserDAO parkingUserDAO;
    private LoggingService loggingService;
    
    public UserAccessService(ParkingUserDAO parkingUserDAO, LoggingService loggingService) {
        this.parkingUserDAO = parkingUserDAO;
        this.loggingService = loggingService;
    }
    
    public ParkingUser findUser(int userID) {
        return parkingUserDAO.findUser(String.valueOf(userID));
    }
    
    public boolean updateAccess(int userID, boolean status) {
        return parkingUserDAO.updateAccessStatus(String.valueOf(userID), status);
    }
    
    public boolean validateAccess(int userID) {
        ParkingUser user = findUser(userID);
        return user != null;
    }
    
    public AccessChangeResult confirmAccessChange(int userID, boolean status) {
        boolean success = updateAccess(userID, status);
        return new AccessChangeResult(success, success ? "Access updated successfully" : "Failed to update access");
    }
}

