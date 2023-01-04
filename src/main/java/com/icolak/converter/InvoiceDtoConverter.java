package com.icolak.converter;

import com.icolak.dto.InvoiceDTO;
import com.icolak.service.InvoiceService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDtoConverter implements Converter<String, InvoiceDTO> {

    private final InvoiceService invoiceService;

    public InvoiceDtoConverter(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceDTO convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return invoiceService.findById(Long.parseLong(source));
    }
}
