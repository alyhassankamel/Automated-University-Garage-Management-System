package entity;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class GateSensor extends Sensor {

    /**
     * Default constructor
     */
    public GateSensor() {
    }

    /**
     * 
     */
    private String gateID;


    /**
     * @return
     */
    public String scanAndSendLicensePlate() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public GateStatus monitorGateStatus() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void detectGateError() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public SensorAlert generateGateErrorAlert() {
        // TODO implement here
        return null;
    }

    /**
     * @param alert 
     * @return
     */
    public void forwardAlert(SensorAlert alert) {
        // TODO implement here
        return null;
    }

}