package com.icolak.service.implementation;

import com.icolak.dto.InvoiceDTO;
import com.icolak.enums.InvoiceType;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.InvoiceRepository;
import com.icolak.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil) {
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceDTO findById(Long id) {
        return mapperUtil.convert(invoiceRepository.findById(id).orElseThrow(), new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> listAllPurchaseInvoices() {
        return invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getInvoiceType().getValue().equals("Purchase"))
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO()))
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
