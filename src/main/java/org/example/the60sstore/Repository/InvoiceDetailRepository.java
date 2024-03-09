package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {

    List<InvoiceDetail> findByInvoice_InvoiceId(int invoiceId);
}
