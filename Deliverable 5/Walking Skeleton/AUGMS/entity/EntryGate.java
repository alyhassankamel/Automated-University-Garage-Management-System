package entity;

public class EntryGate {

    private String gateID;
    private GateStatus status;

    public EntryGate() {
        this.status = GateStatus.CLOSED;
    }

    public void openGate() {
        this.status = GateStatus.OPEN;
        System.out.println("Entry Gate opened.");
    }

    public void closeGate() {
        this.status = GateStatus.CLOSED;
        System.out.println("Entry Gate closed.");
    }

    public void confirmGateOpened() {
        // simulation stub
        System.out.println("Gate open confirmed.");
    }

    public GateStatus getGateStatus() {
        return this.status;
    }
}