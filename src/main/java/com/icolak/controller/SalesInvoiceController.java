package com.icolak.controller;

import com.icolak.dto.InvoiceDTO;
import com.icolak.enums.ClientVendorType;
import com.icolak.enums.InvoiceType;
import com.icolak.service.ClientVendorService;
import com.icolak.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;

    public SalesInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String getSalesInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.listAllInvoicesByTypeAndCompany(InvoiceType.SALES));
        return "/invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String createSalesInvoice(Model model) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNo(invoiceService.generateInvoiceNo(InvoiceType.SALES));
        invoiceDTO.setDate(LocalDate.now());
        model.addAttribute("newSalesInvoice", invoiceDTO);
        model.addAttribute("clients", clientVendorService.listClientVendorsByTypeAndCompany(ClientVendorType.CLIENT));
        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String insertSalesInvoice(@ModelAttribute("newSalesInvoice") InvoiceDTO invoiceDTO) {
        invoiceService.save(invoiceDTO);
        return "redirect:/salesInvoices/update" + invoiceDTO.getId();
    }
}
