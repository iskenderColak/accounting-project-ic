package com.icolak.service;

import com.icolak.dto.InvoiceProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {
    InvoiceProductDTO findById(Long id);

    boolean isExistByProductId(Long id);

    List<InvoiceProductDTO> listByInvoiceId(Long id);

    void save(InvoiceProductDTO invoiceProductDTO, Long id);

    BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo);

    BigDecimal getTotalPriceWithoutTaxByInvoice(String invoiceNo);

    void deleteInvoiceProductById(Long invoiceProductId);
}
