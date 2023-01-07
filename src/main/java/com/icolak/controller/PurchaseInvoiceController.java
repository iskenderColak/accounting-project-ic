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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public PurchaseInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService, InvoiceProductService invoiceProductService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public String getPurchaseInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.listAllInvoicesByTypeAndCompany(InvoiceType.PURCHASE));
        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {
        InvoiceDTO invoiceDTO = invoiceService.findById(id);
        invoiceDTO.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDTO.getInvoiceNo()));
        invoiceDTO.setPrice(invoiceProductService.getTotalPriceWithoutTaxByInvoice(invoiceDTO.getInvoiceNo()));
        invoiceDTO.setTax(invoiceDTO.getTotal().subtract(invoiceDTO.getPrice()));
        model.addAttribute("company", invoiceDTO.getCompany());
        model.addAttribute("invoice", invoiceDTO);
        model.addAttribute("invoiceProducts", invoiceProductService.listByInvoiceId(id));
        return "/invoice/invoice_print";
    }

    @GetMapping("/create")
    public String createPurchaseInvoice(Model model) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNo(invoiceService.generateInvoiceNo(InvoiceType.PURCHASE));
        invoiceDTO.setDate(LocalDate.now());
        model.addAttribute("newPurchaseInvoice", invoiceDTO);
        model.addAttribute("vendors", clientVendorService.listClientVendorsByTypeAndCompany(ClientVendorType.VENDOR));
        return "/invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String insertPurchaseInvoice(@ModelAttribute("newPurchaseInvoice") InvoiceDTO invoiceDTO) {
        invoiceService.save(invoiceDTO);
        return "redirect:/purchaseInvoices/addInvoiceProduct/" + invoiceDTO.getId();
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String insertProductInvoice(@PathVariable("invoiceId") Long invoiceId,
                                              @ModelAttribute("newInvoiceProduct") InvoiceProductDTO invoiceProductDTO) {
        invoiceProductService.save(invoiceProductDTO, invoiceId);
        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @GetMapping({"/update/{id}", "/addInvoiceProduct/{id}"})
    public String editPurchaseInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("vendors", clientVendorService.listClientVendorsByTypeAndCompany(ClientVendorType.VENDOR));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("products", productService.listAllProductsByCompany());
        model.addAttribute("invoiceProducts", invoiceProductService.listByInvoiceId(id));
        return "/invoice/purchase-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String updatePurchaseInvoice(@ModelAttribute("invoice") InvoiceDTO invoiceDTO) {
        invoiceService.update(invoiceDTO);
        return "redirect:/purchaseInvoices/update/" + invoiceDTO.getId();
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String deleteProductFromPurchaseInvoice(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.deleteInvoiceProductById(invoiceProductId);
        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @GetMapping("/delete/{id}")
    public String deletePurchaseInvoice(@PathVariable("id") Long id) {
        invoiceService.delete(id);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approvePurchaseInvoice(@PathVariable("id") Long id) {
        invoiceService.approve(id);
        return "redirect:/purchaseInvoices/list";
    }
}





















