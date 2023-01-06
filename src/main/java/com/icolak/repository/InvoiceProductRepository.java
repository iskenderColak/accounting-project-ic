package com.icolak.repository;

import com.icolak.entity.InvoiceProduct;
import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    boolean existsByProductId(Long id);

    List<InvoiceProduct> findAllByInvoiceId(Long id);

    List<InvoiceProduct> findByInvoice_InvoiceNoAndInvoice_Company_Id(String invoiceNo, Long companyId);

    List<InvoiceProduct> findAllByInvoiceCompanyIdAndInvoiceInvoiceTypeAndInvoiceInvoiceStatus(Long id, InvoiceType type, InvoiceStatus status);
}
