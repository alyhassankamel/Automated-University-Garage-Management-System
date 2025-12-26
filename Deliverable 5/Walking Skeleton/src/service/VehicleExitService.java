package com.augms.service;

import com.augms.dao.VehicleDAO;
import com.augms.dao.ParkingGarageDAO;
import com.augms.dao.ExitGateDAO;
import com.augms.dao.OccupancySensorDAO;
import com.augms.entity.SpotStatus;
import java.time.Duration;
import java.util.Date;

public class VehicleExitService {
    private VehicleDAO vehicleDAO;
    private ParkingGarageDAO parkingGarageDAO;
    private ExitGateDAO exitGateDAO;
    private OccupancySensorDAO occupancySensorDAO;
    private LoggingService loggingService;
    
    public VehicleExitService(VehicleDAO vehicleDAO, ParkingGarageDAO parkingGarageDAO,
                              ExitGateDAO exitGateDAO, OccupancySensorDAO occupancySensorDAO,
                              LoggingService loggingService) {
        this.vehicleDAO = vehicleDAO;
        this.parkingGarageDAO = parkingGarageDAO;
        this.exitGateDAO = exitGateDAO;
        this.occupancySensorDAO = occupancySensorDAO;
        this.loggingService = loggingService;
    }
    
    public boolean validateVehicle(String licensePlate) {
        return vehicleDAO.findVehicleByLicensePlate(licensePlate) != null;
    }
    
    public Duration getParkingDuration(String licensePlate) {
        // Get parking duration for vehicle
        return Duration.ofHours(2);
    }
    
    public ExitResult processExit(String licensePlate, Date timestamp) {
        // Process vehicle exit
        return new ExitResult(true, "Exit processed");
    }
    
    public boolean openExitGate(String gateID) {
        return exitGateDAO.openGate(gateID);
    }
    
    public boolean updateSpotStatus(String spotID, SpotStatus status) {
        // Update spot status
        return true;
    }
    
    public double calculateFee(Duration duration) {
        // Calculate parking fee based on duration
        return duration.toHours() * 5.0;
    }
    
    public boolean recordExit(String licensePlate, Date timestamp, double fee) {
        return loggingService.recordExit(licensePlate, timestamp, fee);
    }
}

