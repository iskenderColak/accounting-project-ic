package com.icolak.controller;

import com.icolak.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;

    public PurchaseInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/list")
    public String getPurchaseInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.listAllPurchaseInvoices());
        return "/invoice/purchase-invoice-list";
    }
}
