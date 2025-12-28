package AUGMS.entity;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 
 */
public class Payment {

    /**
     * Default constructor
     */
    public Payment() {
    }

    /**
     * 
     */
    private String paymentID;

    /**
     * 
     */
    private Double amount;

    /**
     * 
     */
    private LocalDateTime date;


    /**
     * 
     */
    private PaymentStatus status;



    /**
     * @return
     */
    public String getPaymentID() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public PaymentResult processPayment() {
        // TODO implement here
        return null;
    }

    /**
     * @param newStatus 
     * @return
     */
    public void updateStatus(PaymentStatus newStatus) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void paymentSuccess() {
        // TODO implement here
        return null;
    }

    /**
     * @param reason 
     * @return
     */
    public void paymentFailed(String reason) {
        // TODO implement here
        return null;
    }

}
