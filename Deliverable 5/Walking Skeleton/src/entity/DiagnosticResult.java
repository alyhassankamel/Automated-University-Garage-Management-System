package com.augms.entity;

public class DiagnosticResult {
    private boolean isHealthy;
    private String message;
    
    public DiagnosticResult(boolean isHealthy, String message) {
        this.isHealthy = isHealthy;
        this.message = message;
    }
    
    public boolean isHealthy() {
        return isHealthy;
    }
    
    public String getMessage() {
        return message;
    }
}

