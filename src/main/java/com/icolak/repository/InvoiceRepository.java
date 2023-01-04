package com.icolak.repository;

import com.icolak.entity.Invoice;
import com.icolak.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByInvoiceType(InvoiceType invoiceType);

    boolean existsByClientVendorId(Long id);
    Invoice findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(Long id, InvoiceType invoiceType);
}
