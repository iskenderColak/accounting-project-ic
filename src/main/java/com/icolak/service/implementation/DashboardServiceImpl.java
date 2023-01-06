package com.icolak.service.implementation;

import com.icolak.dto.dashboard.FinancialSummaryDTO;
import com.icolak.service.DashboardService;
import com.icolak.service.InvoiceProductService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceProductService invoiceProductService;

    public DashboardServiceImpl(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public FinancialSummaryDTO financialSummaryForCurrentCompany() {
        return FinancialSummaryDTO.builder()
                .totalSales(invoiceProductService.getTotalSalesForCurrentCompany())
                .totalCost(invoiceProductService.getTotalCostForCurrentCompany())
                .profitLoss(invoiceProductService.getTotalProfitLossForCurrentCompany()).build();
    }
}
