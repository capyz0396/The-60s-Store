package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.Invoice;
import org.example.the60sstore.Repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceByInvoiceId(int invoiceId) {
        return invoiceRepository.getInvoiceByInvoiceId(invoiceId);
    }

    public List<Invoice> getInvoiceByCustomerUserOrderByDateDesc(String username) {
        return invoiceRepository.getInvoiceByCustomer_UsernameOrderByInvoiceDateDesc(username);
    }
}
