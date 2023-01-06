package com.icolak.controller;

import com.icolak.service.DashboardService;
import com.icolak.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;

    public DashboardController(DashboardService dashboardService, InvoiceService invoiceService) {
        this.dashboardService = dashboardService;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String getInvoices(Model model) {

        model.addAttribute("invoices", invoiceService.listAllInvoices());
        model.addAttribute("summaryNumbers", dashboardService.financialSummaryForCurrentCompany());

        return "/dashboard";
    }
}
