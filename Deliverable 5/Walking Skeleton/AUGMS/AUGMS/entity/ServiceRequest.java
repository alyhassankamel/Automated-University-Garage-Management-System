package AUGMS.entity;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 
 */
public class ServiceRequest {

    /**
     * Default constructor
     */
    public ServiceRequest() {
    }

    /**
     * 
     */
    private String requestID;

    /**
     * 
     */
    private LocalDateTime date;

    /**
     * 
     */
    private RequestStatus status;







    /**
     * @return
     */
    public String getRequestID() {
        // TODO implement here
        return "";
    }

    /**
     * @param newStatus 
     * @return
     */
    public void updateStatus(RequestStatus newStatus) {
        // TODO implement here
        return null;
    }

    /**
     * @param userID 
     * @return
     */
    public void associateRequester(String userID) {
        // TODO implement here
        return null;
    }

    /**
     * @param vehicleID 
     * @return
     */
    public void associateVehicle(String vehicleID) {
        // TODO implement here
        return null;
    }

}
