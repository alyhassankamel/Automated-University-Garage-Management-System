package com.augms.dao;

import com.augms.entity.Invoice;
import java.util.List;

public interface InvoiceDAO {
    boolean createInvoice(Invoice invoice);
    Invoice findInvoice(String invoiceID);
    boolean updateInvoice(Invoice invoice);
    List<Invoice> getInvoicesByRequest(String requestID);
}

