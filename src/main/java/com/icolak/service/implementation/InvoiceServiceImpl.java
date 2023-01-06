package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.entity.Invoice;
import com.icolak.enums.ClientVendorType;
import com.icolak.enums.InvoiceStatus;
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
    public List<InvoiceDTO> listAllInvoicesByTypeAndCompany(InvoiceType invoiceType) {
        return setPriceTaxTotalToInvoice(invoiceRepository.findAllByInvoiceTypeAndCompanyIdOrderByInvoiceNoDesc(invoiceType, currentCompanyId()));
    }

    @Override
    public boolean existsByClientVendorId(Long id) {
        return invoiceRepository.existsByClientVendorId(id);
    }

    @Override
    public String generateInvoiceNo(InvoiceType invoiceType) {
        Invoice invoice = invoiceRepository.findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(
                securityService.getLoggedInUser().getCompany().getId(), invoiceType);
        int number = Integer.parseInt(invoice.getInvoiceNo().substring(2)) + 1;
        String prefix;
        if (invoice.getInvoiceType().equals(InvoiceType.PURCHASE)) {
            prefix = "P-";
        } else {
            prefix = "S-";
        }
        if (number < 10) {
            return prefix + "00" + number;
        } else if (number < 100) {
            return prefix + "0" + number;
        }
        return prefix + number;
    }

    @Override
    public void save(InvoiceDTO invoiceDto) {
        if (invoiceDto.getClientVendor().getClientVendorType().equals(ClientVendorType.CLIENT)) {
            invoiceDto.setInvoiceType(InvoiceType.SALES);
        } else {
            invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        }

        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoiceRepository.save(invoice);
        invoiceDto.setId(invoice.getId());
    }

    @Override
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setIsDeleted(true);
        invoiceProductService.deleteRelatedInvoiceProducts(id);
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDTO> listLast3ApprovedInvoicesByCompany() {
        return setPriceTaxTotalToInvoice(invoiceRepository.findTop3ByCompanyIdAndInvoiceStatusOrderByDateDesc(currentCompanyId(), InvoiceStatus.APPROVED));
    }

    private List<InvoiceDTO> setPriceTaxTotalToInvoice(List<Invoice> list) {
        return list.stream()
                .map(invoice -> {
                    InvoiceDTO dto = mapperUtil.convert(invoice, new InvoiceDTO());
                    dto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoice.getInvoiceNo()));
                    dto.setPrice(invoiceProductService.getTotalPriceWithoutTaxByInvoice(invoice.getInvoiceNo()));
                    dto.setTax(dto.getTotal().subtract(dto.getPrice()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Long currentCompanyId() {
        return securityService.getLoggedInUser().getCompany().getId();
    }
}
