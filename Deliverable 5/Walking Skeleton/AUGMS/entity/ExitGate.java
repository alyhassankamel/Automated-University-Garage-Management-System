package entity;

public class ExitGate {
    
    private String gateId;

    public ExitGate() {
    }

    public ExitGate(String gateId) {
        this.gateId = gateId;
    }

    public void openGate() {
        System.out.println("ExitGate " + gateId + " opening...");
    }

    public void closeGate() {
        System.out.println("ExitGate " + gateId + " closing...");
    }
}