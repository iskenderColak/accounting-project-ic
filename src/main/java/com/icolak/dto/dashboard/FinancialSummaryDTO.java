package com.icolak.dto.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinancialSummaryDTO {
    private BigDecimal totalCost;
    private BigDecimal totalSales;
    private BigDecimal profitLoss;
}
