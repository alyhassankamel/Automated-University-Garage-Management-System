package com.augms.entity;

public class ExitResult {
    private boolean success;
    private String message;
    
    public ExitResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}

