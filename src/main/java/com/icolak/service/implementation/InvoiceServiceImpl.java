package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.dto.InvoiceProductDTO;
import com.icolak.enums.InvoiceType;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.InvoiceRepository;
import com.icolak.service.InvoiceProductService;
import com.icolak.service.InvoiceService;
import com.icolak.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil, InvoiceProductService invoiceProductService, SecurityService securityService) {
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
        this.invoiceProductService = invoiceProductService;
        this.securityService = securityService;
    }

    @Override
    public InvoiceDTO findById(Long id) {
        return mapperUtil.convert(invoiceRepository.findById(id).orElseThrow(), new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> listAllPurchaseInvoices() {
        return invoiceRepository.findAll().stream()
                .filter(invoice -> (invoice.getInvoiceType().getValue().equals("Purchase")) &&
                        (invoice.getCompany().getId().equals(securityService.getLoggedInUser().getCompany().getId())))
                .map(invoice -> {
                    InvoiceDTO dto = mapperUtil.convert(invoice, new InvoiceDTO());
                    List<InvoiceProductDTO> invoiceProductDTOS = invoiceProductService.listByInvoiceId(invoice.getId());
                    dto.setInvoiceProducts(invoiceProductDTOS);
                    dto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoiceId(invoice.getId()));
                    dto.setPrice(invoiceProductService.getTotalPriceWithoutTaxByInvoiceId(invoice.getId()));
                    dto.setTax(dto.getTotal().subtract(dto.getPrice()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> listAllInvoicesByType(InvoiceType invoiceType) {
        return invoiceRepository.findAllByInvoiceType(invoiceType).stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> listAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByClientVendorId(Long id) {
        return invoiceRepository.existsByClientVendorId(id);
    }
}
