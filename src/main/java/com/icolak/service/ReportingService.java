package com.icolak.service;

import com.icolak.dto.InvoiceProductDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportingService {
    Map<String, BigDecimal> getAllProfitLossPerMonth();

    List<InvoiceProductDTO> getAllInvoiceProductsThatApprovedForCurrentCompany();
}

