package com.augms.service;

import com.augms.dao.ParkingUserDAO;
import com.augms.dao.VehicleDAO;
import com.augms.entity.ParkingUser;
import com.augms.dto.Credentials;
import java.util.List;

public class VehicleManagementService {
    private ParkingUserDAO parkingUserDAO;
    private VehicleDAO vehicleDAO;
    private AuthenticationService authenticationService;
    private LoggingService loggingService;
    
    public VehicleManagementService(ParkingUserDAO parkingUserDAO, VehicleDAO vehicleDAO,
                                   AuthenticationService authenticationService,
                                   LoggingService loggingService) {
        this.parkingUserDAO = parkingUserDAO;
        this.vehicleDAO = vehicleDAO;
        this.authenticationService = authenticationService;
        this.loggingService = loggingService;
    }
    
    public boolean validateCredentials(Credentials credentials) {
        return authenticationService.validateCredentials(credentials);
    }
    
    public List<ParkingUser> getPendingRegistrations() {
        return parkingUserDAO.getPendingRegistrations();
    }
    
    public boolean acceptUser(String userID) {
        ParkingUser user = parkingUserDAO.findUser(userID);
        if (user != null) {
            user.setRegistrationStatus(com.augms.entity.RegistrationStatus.APPROVED);
            user.setAccessStatus(true);
            return parkingUserDAO.updateUser(user);
        }
        return false;
    }
    
    public boolean rejectUser(String userID) {
        ParkingUser user = parkingUserDAO.findUser(userID);
        if (user != null && !user.isUrgentMember()) {
            user.setRegistrationStatus(com.augms.entity.RegistrationStatus.REJECTED);
            user.setAccessStatus(false);
            return parkingUserDAO.updateUser(user);
        }
        return false;
    }
    
    public boolean checkIfUrgentMember(String userID) {
        ParkingUser user = parkingUserDAO.findUser(userID);
        return user != null && user.isUrgentMember();
    }
    
    public boolean grantAccess(String userID) {
        return parkingUserDAO.updateAccessStatus(userID, true);
    }
    
    public boolean forbidAccess(String userID) {
        return parkingUserDAO.updateAccessStatus(userID, false);
    }
}

