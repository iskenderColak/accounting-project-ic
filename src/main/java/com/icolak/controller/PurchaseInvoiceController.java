package com.icolak.controller;

import com.icolak.dto.InvoiceDTO;
import com.icolak.service.ClientVendorService;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/list")
    public String getPurchaseInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.listAllPurchaseInvoices());
        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {
        InvoiceDTO invoiceDTO = invoiceService.findById(id);
        model.addAttribute("company", invoiceDTO.getCompany());
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("invoiceProducts", invoiceProductService.listByInvoiceId(id));
        return "/invoice/invoice_print";
    }
}
