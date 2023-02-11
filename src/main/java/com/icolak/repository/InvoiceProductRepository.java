package com.icolak.repository;

import com.icolak.entity.InvoiceProduct;
import com.icolak.enums.InvoiceStatus;
import com.icolak.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    boolean existsByProductId(Long id);

    List<InvoiceProduct> findAllByInvoiceId(Long id);

    List<InvoiceProduct> findAllByInvoiceCompanyId(Long companyId);

    List<InvoiceProduct> findByInvoice_IdAndInvoice_Company_Id(Long invoiceId, Long companyId);

    List<InvoiceProduct> findAllByInvoiceCompanyIdAndInvoiceInvoiceTypeAndInvoiceInvoiceStatus(Long companyId, InvoiceType type, InvoiceStatus status);

    @Query("SELECT ip FROM InvoiceProduct ip " +
            "WHERE ip.product.id = ?1 AND ip.remainingQuantity > 0 AND ip.invoice.invoiceType = 'PURCHASE' AND ip.invoice.invoiceStatus = 'APPROVED' " +
            "ORDER BY ip.invoice.lastUpdateDateTime")
    List<InvoiceProduct> findAllByProductIdAndRemainingQuantityGreaterThanZeroAndPurchaseApprovedInvoiceOrderByLastUpdateDateTime(Long productId);

    @Query("select ip from InvoiceProduct ip " +
            "where ip.invoice.company.id = :id and ip.invoice.invoiceStatus = 'APPROVED' " +
            "order by ip.invoice.date DESC")
    List<InvoiceProduct> findByInvoice_Company_IdAndInvoice_InvoiceStatusIsApprovedOrderByInvoice_DateDesc(@Param("id") Long companyId);

}