package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Invoice getInvoiceByInvoiceId(int invoiceId);

    List<Invoice> getInvoiceByCustomer_UsernameOrderByInvoiceDateDesc(String username);
}
