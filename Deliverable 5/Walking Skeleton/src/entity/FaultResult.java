package com.augms.entity;

public class FaultResult {
    private boolean hasFault;
    private String faultDescription;
    
    public FaultResult(boolean hasFault, String faultDescription) {
        this.hasFault = hasFault;
        this.faultDescription = faultDescription;
    }
    
    public boolean hasFault() {
        return hasFault;
    }
    
    public String getFaultDescription() {
        return faultDescription;
    }
}

