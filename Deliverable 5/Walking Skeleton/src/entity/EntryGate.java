package com.augms.entity;

public class EntryGate {
    private String gateID;
    private GateStatus status;
    private String garageID;
    
    public EntryGate() {}
    
    public EntryGate(String gateID, GateStatus status, String garageID) {
        this.gateID = gateID;
        this.status = status;
        this.garageID = garageID;
    }
    
    public String getGateID() {
        return gateID;
    }
    
    public void setGateID(String gateID) {
        this.gateID = gateID;
    }
    
    public GateStatus getStatus() {
        return status;
    }
    
    public void setStatus(GateStatus status) {
        this.status = status;
    }
    
    public String getGarageID() {
        return garageID;
    }
    
    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }
    
    public boolean openGate() {
        this.status = GateStatus.OPEN;
        return true;
    }
    
    public boolean closeGate() {
        this.status = GateStatus.CLOSED;
        return true;
    }
    
    public boolean confirmGateOpened() {
        return status == GateStatus.OPEN;
    }
    
    public GateStatus getGateStatus() {
        return status;
    }
}

