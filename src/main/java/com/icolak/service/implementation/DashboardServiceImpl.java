package com.icolak.service.implementation;

import com.icolak.client.ExchangeClient;
import com.icolak.dto.currency.DataDTO;
import com.icolak.dto.dashboard.FinancialSummaryDTO;
import com.icolak.service.DashboardService;
import com.icolak.service.InvoiceProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceProductService invoiceProductService;
    private final ExchangeClient exchangeClient;
    @Value("${apikey}")
    private String apikey;

    public DashboardServiceImpl(InvoiceProductService invoiceProductService, ExchangeClient exchangeClient) {
        this.invoiceProductService = invoiceProductService;
        this.exchangeClient = exchangeClient;
    }

    @Override
    public FinancialSummaryDTO financialSummaryForCurrentCompany() {
        return FinancialSummaryDTO.builder()
                .totalSales(invoiceProductService.getTotalSalesForCurrentCompany())
                .totalCost(invoiceProductService.getTotalCostForCurrentCompany())
                .profitLoss(invoiceProductService.getTotalProfitLossForCurrentCompany()).build();
    }

    @Override
    public DataDTO getExchangeRates() {
        return exchangeClient.getExchangeRates(apikey).getDataDTO();
    }
}
