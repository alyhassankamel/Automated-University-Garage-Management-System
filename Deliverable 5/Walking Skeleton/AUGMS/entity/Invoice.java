package entity;

import java.io.*;
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
    private void date;


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
    public Set<void> getPayments() {
        // TODO implement here
        return null;
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