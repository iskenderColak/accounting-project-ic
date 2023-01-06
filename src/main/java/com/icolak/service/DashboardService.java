package com.icolak.service;

import com.icolak.dto.dashboard.FinancialSummaryDTO;

public interface DashboardService {
    FinancialSummaryDTO financialSummaryForCurrentCompany();
}
