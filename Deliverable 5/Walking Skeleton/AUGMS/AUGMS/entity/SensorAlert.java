package AUGMS.entity;

import java.util.Date;

/**
 * Represents a sensor alert
 */
public class SensorAlert {
    private String alertID;
    private String sensorID;
    private String message;
    private Date timestamp;
    private String severity;

    public SensorAlert(String sensorID, String message, String severity) {
        this.alertID = "ALERT-" + System.currentTimeMillis();
        this.sensorID = sensorID;
        this.message = message;
        this.severity = severity;
        this.timestamp = new Date();
    }

    public String getAlertID() {
        return alertID;
    }

    public String getSensorID() {
        return sensorID;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return String.format("SensorAlert[%s: %s - %s]", sensorID, severity, message);
    }
}

