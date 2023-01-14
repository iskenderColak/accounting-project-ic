package com.icolak.service;

import com.icolak.dto.currency.DataDTO;
import com.icolak.dto.dashboard.FinancialSummaryDTO;

public interface DashboardService {

    FinancialSummaryDTO financialSummaryForCurrentCompany();

    DataDTO getExchangeRates();
}
