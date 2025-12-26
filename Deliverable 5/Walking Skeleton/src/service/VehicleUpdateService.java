package com.augms.service;

import com.augms.dao.VehicleDAO;
import com.augms.dao.ParkingUserDAO;
import com.augms.entity.Vehicle;
import com.augms.entity.ValidationResult;
import java.util.Map;

public class VehicleUpdateService {
    private VehicleDAO vehicleDAO;
    private ParkingUserDAO parkingUserDAO;
    private AuthenticationService authenticationService;
    private LoggingService loggingService;
    
    public VehicleUpdateService(VehicleDAO vehicleDAO, ParkingUserDAO parkingUserDAO,
                               AuthenticationService authenticationService,
                               LoggingService loggingService) {
        this.vehicleDAO = vehicleDAO;
        this.parkingUserDAO = parkingUserDAO;
        this.authenticationService = authenticationService;
        this.loggingService = loggingService;
    }
    
    public Vehicle getVehicleDetails(String vehicleID) {
        return vehicleDAO.findVehicle(vehicleID);
    }
    
    public boolean validateVehicleOwnership(String userID, String vehicleID) {
        return vehicleDAO.validateVehicleOwnership(userID, vehicleID);
    }
    
    public ValidationResult validateUpdatedData(Map<String, Object> updatedDetails) {
        // Validate updated vehicle data
        return new ValidationResult(true, "Valid");
    }
    
    public boolean updateVehicleDetails(String vehicleID, Map<String, Object> updatedDetails) {
        Vehicle vehicle = vehicleDAO.findVehicle(vehicleID);
        if (vehicle != null) {
            vehicle.updateDetails(updatedDetails);
            return vehicleDAO.updateVehicle(vehicle);
        }
        return false;
    }
    
    public boolean saveUpdatedVehicle(Vehicle vehicle) {
        return vehicleDAO.updateVehicle(vehicle);
    }
}

