package org.example.the60sstore.Service;

import org.example.the60sstore.Entity.InvoiceDetail;
import org.example.the60sstore.Repository.InvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/* InvoiceDetailService returns InvoiceDetail or list of them to Controller. */
@Service
public class InvoiceDetailService {

    private final InvoiceDetailRepository invoiceDetailRepository;

    /* Service always need to create Repository first. */
    @Autowired
    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository) {
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    /* save method save new information to database. */
    public InvoiceDetail save(InvoiceDetail invoiceDetail) {
        return invoiceDetailRepository.save(invoiceDetail);
    }

    /* findAll method returns all InvoiceDetail in database. */
    public List<InvoiceDetail> findAll() {
        return invoiceDetailRepository.findAll();
    }

    /* findByInvoiceId return list of InvoiceDetail by invoiceId. */
    public List<InvoiceDetail> findByInvoiceId(int invoiceId) {
        return invoiceDetailRepository.findByInvoice_InvoiceId(invoiceId);
    }
}
