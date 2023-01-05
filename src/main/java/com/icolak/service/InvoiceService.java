package com.icolak.service;

import com.icolak.dto.InvoiceDTO;
import com.icolak.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {

    InvoiceDTO findById(Long id);
    List<InvoiceDTO> listAllPurchaseInvoices();

    List<InvoiceDTO> listAllInvoicesByType(InvoiceType invoiceType);

    List<InvoiceDTO> listAllInvoices();

    boolean existsByClientVendorId(Long id);

    String generateInvoiceNo(InvoiceType invoiceType);

    void save(InvoiceDTO invoiceDTO);
}

