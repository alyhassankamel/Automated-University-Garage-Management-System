package com.augms.entity;

import java.util.Date;

public class ErrorLog {
    private String logID;
    private String sensorID;
    private String errorType;
    private Date date;
    private Date time;
    private String details;
    
    public ErrorLog() {}
    
    public ErrorLog(String logID, String sensorID, String errorType, 
                   Date date, Date time, String details) {
        this.logID = logID;
        this.sensorID = sensorID;
        this.errorType = errorType;
        this.date = date;
        this.time = time;
        this.details = details;
    }
    
    public String getLogID() {
        return logID;
    }
    
    public void setLogID(String logID) {
        this.logID = logID;
    }
    
    public String getSensorID() {
        return sensorID;
    }
    
    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
}

