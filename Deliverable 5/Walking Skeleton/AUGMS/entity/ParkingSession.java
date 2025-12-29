package entity;

import java.sql.Timestamp;

/**
 * Entity class for ParkingSession table.
 */
public class ParkingSession {

    private int sessionId;
    private int vehicleId;
    private int spotId;
    private Timestamp entryTime;
    private Timestamp exitTime;
    private Timestamp processedAt;

    public ParkingSession() {
    }

    public ParkingSession(int sessionId, int vehicleId, int spotId, Timestamp entryTime, Timestamp exitTime, Timestamp processedAt) {
        this.sessionId = sessionId;
        this.vehicleId = vehicleId;
        this.spotId = spotId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.processedAt = processedAt;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }

    public void setExitTime(Timestamp exitTime) {
        this.exitTime = exitTime;
    }

    public Timestamp getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Timestamp processedAt) {
        this.processedAt = processedAt;
    }
}
