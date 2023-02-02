package com.icolak.service.implementation;

import com.icolak.dto.InvoiceProductDTO;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.ReportingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final InvoiceProductService invoiceProductService;

    public ReportingServiceImpl(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public Map<String, BigDecimal> getAllProfitLossPerMonth() {
        return invoiceProductService.getAllApprovedInvoicesForCurrentCompany()
                .stream()
                .collect(Collectors.toMap(invoiceProductDTO -> invoiceProductDTO.getInvoice()
                        .getDate()
                        .getYear() + " " + invoiceProductDTO.getInvoice()
                        .getDate()
                        .getMonth(), InvoiceProductDTO::getProfitLoss, (BigDecimal::add)));
    }

    @Override
    public List<InvoiceProductDTO> getAllInvoiceProductsThatApprovedForCurrentCompany() {
        return invoiceProductService.getAllApprovedInvoicesForCurrentCompany();
    }
}
