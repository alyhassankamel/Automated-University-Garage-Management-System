package com.augms.dao;

import com.augms.entity.ParkingUser;
import java.util.List;

public interface ParkingUserDAO {
    boolean createUser(ParkingUser user);
    ParkingUser findUser(String userID);
    ParkingUser findUserByUniID(String uniID);
    boolean updateUser(ParkingUser user);
    boolean updateAccessStatus(String userID, boolean status);
    List<ParkingUser> getPendingRegistrations();
    boolean deleteUser(String userID);
}

