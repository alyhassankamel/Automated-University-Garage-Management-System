package entity;

import service.GarageSystem;

import java.sql.Timestamp;

public class ExitSensor {
    private int sensorId;
    private GarageSystem garageSystem;

    public ExitSensor(int sensorId, GarageSystem garageSystem) {
        this.sensorId = sensorId;
        this.garageSystem = garageSystem;
    }

    public void detectVehicle(String licensePlate, int gateId) {
        System.out.println("Sensor " + sensorId + " detected vehicle: " + licensePlate);
        // Delegate to GarageSystem
        String result = garageSystem.processExit(licensePlate, new Timestamp(System.currentTimeMillis()), gateId);
        System.out.println(result);
    }
}
