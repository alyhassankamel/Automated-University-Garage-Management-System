package com.augms.entity;

import java.util.Date;

public class Log {
    private String logID;
    private String userID;
    private String vehicleID;
    private String actionType;
    private Date date;
    private Date time;
    private String details;
    private String type;
    
    public Log() {}
    
    public Log(String logID, String userID, String vehicleID, String actionType, 
              Date date, Date time, String details, String type) {
        this.logID = logID;
        this.userID = userID;
        this.vehicleID = vehicleID;
        this.actionType = actionType;
        this.date = date;
        this.time = time;
        this.details = details;
        this.type = type;
    }
    
    public String getLogID() {
        return logID;
    }
    
    public void setLogID(String logID) {
        this.logID = logID;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getVehicleID() {
        return vehicleID;
    }
    
    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}

