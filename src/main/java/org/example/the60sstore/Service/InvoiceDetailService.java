package org.example.testspring.Service;

import org.example.testspring.Entity.InvoiceDetail;
import org.example.testspring.Repository.InvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceDetailService {

    private final InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository) {
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    public InvoiceDetail save(InvoiceDetail invoiceDetail) {
        return invoiceDetailRepository.save(invoiceDetail);
    }

    public List<InvoiceDetail> findAll() {
        return invoiceDetailRepository.findAll();
    }
}
