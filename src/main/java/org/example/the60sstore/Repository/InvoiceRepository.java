package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    public Invoice getInvoiceByInvoiceId(int invoiceId);
}
