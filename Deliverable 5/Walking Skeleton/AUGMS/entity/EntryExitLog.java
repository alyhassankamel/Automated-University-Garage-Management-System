package entity;

import java.sql.Timestamp;

/**
 * Entity class for EntryExitLog table.
 */
public class EntryExitLog {

    private int logId;
    private String licensePlate;
    private String direction; // 'ENTRY' or 'EXIT'
    private int garageId;
    private Integer spotId; // Nullable
    private Timestamp eventTime;

    public EntryExitLog() {
    }

    public EntryExitLog(int logId, String licensePlate, String direction, int garageId, Integer spotId,
            Timestamp eventTime) {
        this.logId = logId;
        this.licensePlate = licensePlate;
        this.direction = direction;
        this.garageId = garageId;
        this.spotId = spotId;
        this.eventTime = eventTime;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public Integer getSpotId() {
        return spotId;
    }

    public void setSpotId(Integer spotId) {
        this.spotId = spotId;
    }

    public Timestamp getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }
}