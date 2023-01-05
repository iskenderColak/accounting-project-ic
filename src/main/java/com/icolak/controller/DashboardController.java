package com.icolak.controller;

import com.icolak.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final InvoiceService invoiceService;

    public DashboardController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/dashboard")
    public String getInvoices(Model model) {

        model.addAttribute("invoices", invoiceService.listAllInvoices());

        return "/dashboard";
    }
}
