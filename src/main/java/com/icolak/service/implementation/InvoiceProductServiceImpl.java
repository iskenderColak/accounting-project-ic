package com.icolak.service.implementation;

import com.icolak.repository.InvoiceProductRepository;
import com.icolak.service.InvoiceProductService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository) {
        this.invoiceProductRepository = invoiceProductRepository;
    }

    @Override
    public boolean isExistByProductId(Long id) {
        return invoiceProductRepository.existsByProductId(id);
    }
}
