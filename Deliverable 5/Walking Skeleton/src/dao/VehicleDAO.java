package com.augms.dao;

import com.augms.entity.Vehicle;
import java.util.List;

public interface VehicleDAO {
    boolean createVehicle(Vehicle vehicle);
    Vehicle findVehicle(String vehicleID);
    Vehicle findVehicleByLicensePlate(String licensePlate);
    boolean updateVehicle(Vehicle vehicle);
    boolean deleteVehicle(String vehicleID);
    boolean validateVehicleOwnership(String userID, String vehicleID);
    List<Vehicle> getVehiclesByUser(String userID);
}

