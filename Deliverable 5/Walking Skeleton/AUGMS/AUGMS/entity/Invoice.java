package AUGMS.entity;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 
 */
public class Invoice {

    /**
     * Default constructor
     */
    public Invoice() {
    }

    /**
     * 
     */
    private String invoiceID;

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
    private InvoiceStatus status;



    /**
     * @return
     */
    public String getInvoiceID() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public Double getAmount() {
        // TODO implement here
        return null;
    }

    /**
     * @param newStatus 
     * @return
     */
    public void updateStatus(InvoiceStatus newStatus) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void getPayments() {
        // TODO implement here
    }

    /**
     * @param payment 
     * @return
     */
    public void addPayment(Payment payment) {
        // TODO implement here
        return null;
    }

}
