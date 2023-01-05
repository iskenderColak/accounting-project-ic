package com.icolak.service;

import com.icolak.dto.InvoiceProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {
    boolean isExistByProductId(Long id);
    List<InvoiceProductDTO> listByInvoiceId(Long id);

    BigDecimal getTotalPriceWithTaxByInvoiceId(Long id);

    BigDecimal getTotalPriceWithoutTaxByInvoiceId(Long id);

    void save(InvoiceProductDTO invoiceProductDTO, Long id);
}
