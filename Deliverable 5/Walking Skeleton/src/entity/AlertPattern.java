package com.augms.entity;

public class AlertPattern {
    private String patternID;
    private String description;
    
    public AlertPattern(String patternID, String description) {
        this.patternID = patternID;
        this.description = description;
    }
    
    public String getPatternID() {
        return patternID;
    }
    
    public String getDescription() {
        return description;
    }
}

