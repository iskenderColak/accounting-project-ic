package com.icolak.repository;

import com.icolak.entity.Invoice;
import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByInvoiceTypeAndCompanyIdOrderByLastUpdateDateTimeDesc(InvoiceType invoiceType, Long id);

    boolean existsByClientVendorId(Long id);

    Invoice findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(Long id, InvoiceType invoiceType);

    List<Invoice> findTop3ByCompanyIdAndInvoiceStatusOrderByDateDesc(Long id, InvoiceStatus status);
}
