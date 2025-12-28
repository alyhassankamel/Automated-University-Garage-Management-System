package AUGMS.entity;

import java.sql.Timestamp;

/**
 * Represents a vehicle entry/exit log record.
 */
public class EntryExitLog {

    private int logID;
    private String licensePlate;
    private String type; // ENTRY or EXIT
    private String spotID;
    private Timestamp timestamp;
    private Double fee;

    public EntryExitLog() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public EntryExitLog(String licensePlate, String type, String spotID) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.spotID = spotID;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpotID() {
        return spotID;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }
}

