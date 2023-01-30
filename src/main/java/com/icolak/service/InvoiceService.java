package com.icolak.service;

import com.icolak.dto.InvoiceDTO;
import com.icolak.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {

    InvoiceDTO findById(Long id);

    List<InvoiceDTO> listAllInvoicesByTypeAndCompany(InvoiceType sales);

    boolean existsByClientVendorId(Long id);

    String generateInvoiceNo(InvoiceType invoiceType);

    void save(InvoiceDTO invoiceDTO);

    InvoiceDTO update(InvoiceDTO invoiceDTO);

    void delete(Long id);

    List<InvoiceDTO> listLast3ApprovedInvoicesByCompany();

    void approvePurchaseInvoice(Long id);

    void approveSalesInvoice(Long id);
}

