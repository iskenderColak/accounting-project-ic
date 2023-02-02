package com.icolak.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ReportingService {
    Map<String, BigDecimal> getAllProfitLossPerMonth();
}
