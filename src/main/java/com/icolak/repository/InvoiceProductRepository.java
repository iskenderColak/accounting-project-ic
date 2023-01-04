package com.icolak.repository;

import com.icolak.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    boolean existsByProductId(Long id);
    List<InvoiceProduct> findAllByInvoiceId(Long id);
}
