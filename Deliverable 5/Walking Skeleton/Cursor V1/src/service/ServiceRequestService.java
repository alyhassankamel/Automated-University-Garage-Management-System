package com.augms.service;

import com.augms.dao.ServiceRequestDAO;
import com.augms.dao.ParkingSpotDAO;
import com.augms.dao.ServiceTypeDAO;
import com.augms.dao.ParkingUserDAO;
import com.augms.dao.VehicleDAO;
import com.augms.entity.ParkingSpot;
import com.augms.entity.ServiceType;
import com.augms.entity.ServiceRequest;
import com.augms.entity.Vehicle;
import com.augms.entity.Invoice;
import com.augms.entity.PaymentMethod;
import java.util.Date;
import java.util.List;

public class ServiceRequestService {
    private ServiceRequestDAO serviceRequestDAO;
    private ParkingSpotDAO parkingSpotDAO;
    private ServiceTypeDAO serviceTypeDAO;
    private ParkingUserDAO parkingUserDAO;
    private VehicleDAO vehicleDAO;
    private InvoiceService invoiceService;
    private PaymentService paymentService;
    private LoggingService loggingService;
    
    public ServiceRequestService(ServiceRequestDAO serviceRequestDAO, ParkingSpotDAO parkingSpotDAO,
                                ServiceTypeDAO serviceTypeDAO, ParkingUserDAO parkingUserDAO,
                                VehicleDAO vehicleDAO, InvoiceService invoiceService,
                                PaymentService paymentService, LoggingService loggingService) {
        this.serviceRequestDAO = serviceRequestDAO;
        this.parkingSpotDAO = parkingSpotDAO;
        this.serviceTypeDAO = serviceTypeDAO;
        this.parkingUserDAO = parkingUserDAO;
        this.vehicleDAO = vehicleDAO;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.loggingService = loggingService;
    }
    
    public ParkingSpot getSpotDetails(String spotID) {
        return parkingSpotDAO.findSpot(spotID);
    }
    
    public List<ServiceType> getAvailableServicesForSpot(String spotID) {
        return serviceTypeDAO.getAvailableServicesForSpot(spotID);
    }
    
    public ServiceRequest createServiceRequest(String serviceID, String spotID, String userID) {
        // Create service request
        return null;
    }
    
    public boolean associateRequester(String userID, String requestID) {
        return true;
    }
    
    public boolean associateVehicle(String vehicleID, String requestID) {
        return true;
    }
    
    public Invoice generateInvoice(double amount, Date date, Date time) {
        return invoiceService.generateInvoice(amount, date, time);
    }
    
    public boolean processPayment(PaymentMethod paymentMethod, double amount) {
        return paymentService.processPayment(paymentMethod, amount);
    }
    
    public boolean updateRequestStatus(String requestID, String status) {
        return serviceRequestDAO.updateRequestStatus(requestID, status);
    }
}

