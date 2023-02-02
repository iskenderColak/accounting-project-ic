package com.icolak.service;

import com.icolak.dto.InvoiceProductDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface InvoiceProductService {
    InvoiceProductDTO findById(Long id);

    boolean isExistByProductId(Long id);

    List<InvoiceProductDTO> listByInvoiceId(Long id);

    InvoiceProductDTO save(InvoiceProductDTO invoiceProductDTO, Long InvoiceId);

    BigDecimal getTotalPriceWithTaxByInvoice(Long invoiceId);

    BigDecimal getTotalPriceWithoutTaxByInvoice(Long invoiceId);

    void deleteInvoiceProductById(Long invoiceProductId);

    void deleteRelatedInvoiceProducts(Long invoiceId);

    BigDecimal getTotalSalesForCurrentCompany();

    BigDecimal getTotalCostForCurrentCompany();

    BigDecimal getTotalProfitLossForCurrentCompany();

    List<InvoiceProductDTO> listPurchaseInvoiceProductIncludesProductsOfSalesInvoiceProduct(Long productId);

    void saveSettingsAfterApproving(InvoiceProductDTO invoiceProductDTO);

    List<InvoiceProductDTO> getAllApprovedInvoicesForCurrentCompany();
}