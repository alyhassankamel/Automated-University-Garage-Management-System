package com.augms.dao;

import com.augms.entity.ParkingStatus;
import java.util.Date;
import java.util.List;

public interface ParkingStatusDAO {
    ParkingStatus getParkingStatus(String garageID);
    ParkingStatus getParkingStatusByDate(String garageID, Date date);
    boolean saveParkingStatus(ParkingStatus status);
    List<ParkingStatus> getParkingStatusHistory(String garageID, Date startDate, Date endDate);
}
