package com.icolak.controller;

import com.icolak.dto.InvoiceDTO;
import com.icolak.dto.InvoiceProductDTO;
import com.icolak.enums.ClientVendorType;
import com.icolak.enums.InvoiceType;
import com.icolak.service.ClientVendorService;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.InvoiceService;
import com.icolak.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public SalesInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService, InvoiceProductService invoiceProductService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
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
        return "redirect:/salesInvoices/update/" + invoiceDTO.getId();
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String insertProductInvoice(@PathVariable("invoiceId") Long invoiceId, Model model,
                                       @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDTO invoiceProductDTO, BindingResult bindingResult) {
        if (!invoiceProductService.isStockEnough(invoiceProductDTO)) {
            bindingResult.rejectValue("quantity", "","Stock is not enough");
            model.addAttribute("invoice", invoiceService.findById(invoiceId));
            model.addAttribute("clients", clientVendorService.listClientVendorsByTypeAndCompany(ClientVendorType.CLIENT));
            model.addAttribute("products", productService.listAllProductsByCompany());
            model.addAttribute("invoiceProducts", invoiceProductService.listByInvoiceId(invoiceId));
            return "/invoice/sales-invoice-update";
        }

        invoiceProductService.save(invoiceProductDTO, invoiceId);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    /*
     model.addAttribute("invoice", invoiceService.findById(invoiceId));
        model.addAttribute("clients", clientVendorService.listClientVendorsByTypeAndCompany(ClientVendorType.CLIENT));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("products", productService.listAllProductsByCompany());
        model.addAttribute("invoiceProducts", invoiceProductService.listByInvoiceId(invoiceId));
        try {
            invoiceProductService.saveAfterCheckingStock(invoiceProductDTO, invoiceId);
        } catch (IllegalAccessException e) {
            model.addAttribute("error", e.getMessage());
            return "/invoice/sales-invoice-update";
        }

        return "redirect:/salesInvoices/update/" + invoiceId;
     */

    @GetMapping("/update/{id}")
    public String editSalesInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("clients", clientVendorService.listClientVendorsByTypeAndCompany(ClientVendorType.CLIENT));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("products", productService.listAllProductsByCompany());
        model.addAttribute("invoiceProducts", invoiceProductService.listByInvoiceId(id));
        return "/invoice/sales-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String updateSalesInvoice(@ModelAttribute("invoice") InvoiceDTO invoiceDTO) {
        invoiceService.update(invoiceDTO);
        return "redirect:/salesInvoices/update/" + invoiceDTO.getId();
    }

}
