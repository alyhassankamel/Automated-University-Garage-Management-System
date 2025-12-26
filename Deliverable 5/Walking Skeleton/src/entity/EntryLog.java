package com.augms.entity;

import java.util.Date;

public class EntryLog {
    private String logID;
    private String licensePlate;
    private Date timestamp;
    
    public EntryLog() {}
    
    public EntryLog(String logID, String licensePlate, Date timestamp) {
        this.logID = logID;
        this.licensePlate = licensePlate;
        this.timestamp = timestamp;
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
}

