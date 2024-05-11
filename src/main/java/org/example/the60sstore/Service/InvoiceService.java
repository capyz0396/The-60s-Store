package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Invoice;
import org.example.the60sstore.Repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/* InvoiceService returns Invoice or list of them to Controller. */
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    /* Service always need to create Repository first. */
    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /* save method can save or update by invoice param. */
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    /* getAll method return list of Invoice. */
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    /* getInvoiceByInvoiceId method return Invoice object by invoiceId. */
    public Invoice getInvoiceByInvoiceId(int invoiceId) {
        return invoiceRepository.getInvoiceByInvoiceId(invoiceId);
    }

    /* getInvoiceByCustomerUserOrderByDateDesc method use username to find all invoices and return sort list. */
    public List<Invoice> getInvoiceByCustomerUserOrderByDateDesc(String username) {
        return invoiceRepository.getInvoiceByCustomer_UsernameOrderByInvoiceDateDesc(username);
    }
}
