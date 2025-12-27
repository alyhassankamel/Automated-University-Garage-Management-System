public class ParkingSpot {
    private Long spotId;
    private String spotStatus;
    private Long garageId;

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public String getSpotStatus() {
        return spotStatus;
    }

    public void setSpotStatus(String spotStatus) {
        this.spotStatus = spotStatus;
    }

    public Long getGarageId() {
        return garageId;
    }

    public void setGarageId(Long garageId) {
        this.garageId = garageId;
    }
}