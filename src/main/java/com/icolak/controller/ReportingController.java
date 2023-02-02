package com.icolak.controller;

import com.icolak.service.ReportingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/profitLossData")
    public String listProfitLoss(Model model) {
        model.addAttribute("monthlyProfitLossDataMap", reportingService.getAllProfitLossPerMonth());
        return "report/profit-loss-report";
    }

    @GetMapping("/stockData")
    public String listStockData(Model model) {
        model.addAttribute("invoiceProducts", reportingService.getAllInvoiceProductsThatApprovedForCurrentCompany());
        return "report/stock-report";
    }
}
