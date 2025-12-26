package com.augms.entity;

import java.util.Date;

public class EntryExitLog {
    private String logID;
    private String licensePlate;
    private Date timestamp;
    private String type; // ENTRY or EXIT
    private double fee;
    
    public EntryExitLog() {}
    
    public EntryExitLog(String logID, String licensePlate, Date timestamp, String type, double fee) {
        this.logID = logID;
        this.licensePlate = licensePlate;
        this.timestamp = timestamp;
        this.type = type;
        this.fee = fee;
    }
    
    public String getLogID() {
        return logID;
    }
    
    public void setLogID(String logID) {
        this.logID = logID;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public double getFee() {
        return fee;
    }
    
    public void setFee(double fee) {
        this.fee = fee;
    }
}

