package com.augms.dao;

import com.augms.entity.ServiceRequest;
import java.util.List;

public interface ServiceRequestDAO {
    boolean createRequest(ServiceRequest request);
    ServiceRequest findRequest(String requestID);
    boolean updateRequestStatus(String requestID, String status);
    List<ServiceRequest> getRequestsByUser(String userID);
    List<ServiceRequest> getRequestsBySpot(String spotID);
}

