package entity;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Sensor {

    /**
     * Default constructor
     */
    public Sensor() {
    }

    /**
     * 
     */
    private String sensorID;


    /**
     * @return
     */
    public String getSensorID() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public DiagnosticResult performSelfDiagnostics() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public FaultResult detectFault() {
        // TODO implement here
        return null;
    }

    /**
     * @param details 
     * @return
     */
    public SensorAlert generateErrorAlert(String details) {
        // TODO implement here
        return null;
    }

    /**
     * @param alert 
     * @return
     */
    public void reportAlertToLogging(SensorAlert alert) {
        // TODO implement here
        return null;
    }

}