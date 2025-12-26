package com.augms.entity;

public class EntryValidationResult {
    private boolean isValid;
    private boolean spaceAvailable;
    private String message;
    
    public EntryValidationResult(boolean isValid, boolean spaceAvailable, String message) {
        this.isValid = isValid;
        this.spaceAvailable = spaceAvailable;
        this.message = message;
    }
    
    public boolean isValid() {
        return isValid;
    }
    
    public boolean isSpaceAvailable() {
        return spaceAvailable;
    }
    
    public String getMessage() {
        return message;
    }
}

